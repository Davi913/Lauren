package com.yuhtin.lauren.core.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuhtin.lauren.Lauren;
import com.yuhtin.lauren.core.logger.Logger;
import com.yuhtin.lauren.utils.helper.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.*;

public class TrackManager extends AudioEventAdapter {

    public static final Map<String, String> fields = new HashMap<>();
    public final List<String> repeatedMusics = new ArrayList<>();
    private static TrackManager INSTANCE;

    public final GuildMusicManager musicManager;
    public final AudioPlayerManager audioManager;
    public final AudioPlayer player;
    public boolean repeat;
    public VoiceChannel audio;

    public TrackManager() {
        this.audioManager = new DefaultAudioPlayerManager();
        this.player = audioManager.createPlayer();

        musicManager = new GuildMusicManager(player);
        AudioSourceManagers.registerRemoteSources(audioManager);
        AudioSourceManagers.registerLocalSource(audioManager);

        player.addListener(this);
        Lauren.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
    }

    public static TrackManager get() {
        if (INSTANCE == null) INSTANCE = new TrackManager();

        return INSTANCE;
    }

    public static void constructFields() {
        fields.put("api_dev_key", Lauren.config.pastebinDevKey);
        fields.put("api_user_key", Lauren.config.pastebinUserKey);
        fields.put("api_paste_private", "1");
        fields.put("api_paste_expire_date", "10M");
        fields.put("api_paste_format", "yaml");
        fields.put("api_option", "paste");
        fields.put("api_paste_code", "");
        fields.put("api_paste_name", "");
    }

    public void destroy() {
        if (audio == null) return;

        audio.getGuild().getAudioManager().closeAudioConnection();
        player.stopTrack();
        musicManager.player.destroy();
        purgeQueue();
    }

    public void loadTrack(String trackUrl, Member member, TextChannel channel, boolean message) {
        String emoji = trackUrl.contains("spotify.com") ? "<:spotify:751049445592006707>" : "<:youtube:751031330057486366>";
        channel.sendMessage(emoji + " **Procurando** 🔎 `" + trackUrl.replace("ytsearch:", "") + "`").queue();

        channel.sendTyping().queue();
        audioManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                if (player.isPaused()) player.setPaused(false);
                if (message) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("💿 " + Utilities.INSTANCE.getFullName(member.getUser()) + " adicionou 1 música a fila")
                            .setDescription(
                                    "\ud83d\udcc0 Nome: `" + track.getInfo().title + "`\n" +
                                            "\uD83D\uDCB0 Autor: `" + track.getInfo().author + "`\n" +
                                            "\uD83D\uDCE2 Tipo de vídeo: `" +
                                            (track.getInfo().isStream ? "Stream" : track.getInfo().title.contains("Podcast") ?
                                                    "Podcast" : "Música") + "`\n" +
                                            "\uD83D\uDCCC Link: [Clique aqui](" + track.getInfo().uri + ")");

                    Logger.log("The player " + Utilities.INSTANCE.getFullName(member.getUser()) + " added a music").save();
                    channel.sendMessage(embed.build()).queue();
                }

                audio = member.getVoiceState().getChannel();
                play(track, member);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {
                    if (player.isPaused()) player.setPaused(false);

                    int limit = Utilities.INSTANCE.isBooster(member) || Utilities.INSTANCE.isDJ(member, null, false) ? 100 : 25;
                    int maxMusics = Math.min(playlist.getTracks().size(), limit);

                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("💿 " + Utilities.INSTANCE.getFullName(member.getUser()) + " adicionou " + maxMusics + " músicas a fila")
                            .setDescription("\uD83D\uDCBD Informações da playlist:\n" +
                                    "\ud83d\udcc0 Nome: `" + playlist.getName() + "`\n" +
                                    "\uD83C\uDFB6 Músicas: `" + maxMusics + "`\n\n" +
                                    "\uD83D\uDCCC Link: [Clique aqui](" + trackUrl + ")");

                    Logger.log("The player " + Utilities.INSTANCE.getFullName(member.getUser()) + " added a playlist with " + maxMusics + " musics").save();
                    audio = member.getVoiceState().getChannel();
                    for (int i = 0; i < maxMusics; i++) {
                        play(playlist.getTracks().get(i), member);
                    }

                    channel.sendMessage(embed.build()).queue();
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessage("**Erro** \uD83D\uDCCC `Não encontrei nada relacionado a busca`").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("**Erro** \uD83D\uDCCC `O vídeo ou playlist está privado`").queue();
            }
        });
    }

    public void play(AudioTrack track, Member member) {
        musicManager.scheduler.queue(track, member);
    }

    public void shuffleQueue() {
        List<AudioInfo> tempQueue = new ArrayList<>(this.getQueuedTracks());

        AudioInfo current = tempQueue.get(0);
        tempQueue.remove(0);

        Collections.shuffle(tempQueue);
        tempQueue.add(0, current);

        purgeQueue();
        musicManager.scheduler.queue.addAll(tempQueue);
    }

    public Set<AudioInfo> getQueuedTracks() {
        return new LinkedHashSet<>(musicManager.scheduler.queue);
    }

    public void purgeQueue() {
        musicManager.scheduler.queue.clear();
    }

    public AudioInfo getTrackInfo() {
        return musicManager.scheduler.queue
                .stream()
                .filter(audioInfo -> audioInfo.getTrack().equals(player.getPlayingTrack()))
                .findFirst()
                .orElse(null);
    }
}