package com.yuhtin.lauren.core.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    public final BlockingQueue<AudioInfo> queue;
    private final AudioPlayer player;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track, Member author) {
        AudioInfo info = new AudioInfo(track, author);
        queue.add(info);
        if (this.player.getPlayingTrack() == null) this.player.playTrack(track);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        VoiceChannel voice = TrackManager.get().audio;
        voice.getGuild().getAudioManager().openAudioConnection(voice);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        AudioInfo poll = queue.poll();
        if (poll == null) return;

        Guild guild = poll.getAuthor().getGuild();
        if (queue.isEmpty()) guild.getAudioManager().closeAudioConnection();
        else player.playTrack(queue.element().getTrack());
    }

    /*public void onTrackEnd(boolean force) {
        AudioInfo poll = queue.poll();
        if (poll == null) return;
        if (!force && poll.isRepeat() && !poll.isRepeated()) {
            poll.setRepeated(true);

            AudioTrack audioTrack = poll.getTrack().makeClone();
            poll.setTrack(audioTrack);
            player.playTrack(audioTrack);
            return;
        }

        Guild guild = poll.getAuthor().getGuild();
        if (queue.isEmpty()) guild.getAudioManager().closeAudioConnection();
        else player.playTrack(queue.element().getTrack());
    }*/
}