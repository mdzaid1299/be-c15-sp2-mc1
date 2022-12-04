package com.question.UserTrackService.service;

import com.question.UserTrackService.domain.Track;
import com.question.UserTrackService.domain.User;
import com.question.UserTrackService.exception.TrackNotFoundException;
import com.question.UserTrackService.exception.UserAlreadyExistsException;
import com.question.UserTrackService.exception.UserNotFoundException;
import com.question.UserTrackService.proxy.UserProxy;
import com.question.UserTrackService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProxy userProxy;
    @Override
    public User addUser(User user) throws UserAlreadyExistsException {
        if(userRepository.findById(user.getUserId()).isPresent()){
            throw new UserAlreadyExistsException();
        }
        User user1 = userRepository.save(user);
        if(!(user1.getUserId().isEmpty())){
            ResponseEntity responseEntity = userProxy.saveUser(user);
            System.out.println(responseEntity.getBody());
        }
        return user1;
    }

    @Override
    public User addTrackForUser(String userId, Track track) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId).get();
        if(user.getTrackList() == null){
            user.setTrackList(Arrays.asList(track));
        }else{
            List<Track> tracks = user.getTrackList();
            tracks.add(track);
            user.setTrackList(tracks);
        }
        return userRepository.save(user);
    }

    @Override
    public User deleteTrackFromUser(String userId, int trackId) throws UserNotFoundException, TrackNotFoundException {
        boolean result = false;
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId).get();
        List<Track> tracks = user.getTrackList();
        result = tracks.removeIf(obj->obj.getTrackId()==trackId);
        if(!result)
        {
            throw new TrackNotFoundException();
        }
        user.setTrackList(tracks);
        return userRepository.save(user);
    }

    @Override
    public List<Track> getTrackForUser(String userId) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        return userRepository.findById(userId).get().getTrackList();
    }
    public User updateTrackForUser(String userId,Track track) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId).get();
        List<Track> tracks = user.getTrackList();
        Iterator<Track> iterator = tracks.iterator();
        while (iterator.hasNext()){
            Track track1 = iterator.next();
            if(track1.getTrackId() == track.getTrackId()){
                track1.setTrackName(track.getTrackName());
                track1.setArtistName(track.getArtistName());
            }
        }
        user.setTrackList(tracks);
        return userRepository.save(user);
    }
}
