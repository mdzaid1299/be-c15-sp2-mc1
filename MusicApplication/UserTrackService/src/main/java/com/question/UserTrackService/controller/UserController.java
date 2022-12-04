package com.question.UserTrackService.controller;

import com.question.UserTrackService.domain.Track;
import com.question.UserTrackService.domain.User;
import com.question.UserTrackService.exception.TrackNotFoundException;
import com.question.UserTrackService.exception.UserAlreadyExistsException;
import com.question.UserTrackService.exception.UserNotFoundException;
import com.question.UserTrackService.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@RequestMapping("/trackdata/api/")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user) throws UserAlreadyExistsException {
        ResponseEntity responseEntity = null;
        try{
            user.setTrackList(new ArrayList<>());
            responseEntity = new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
        }catch (UserAlreadyExistsException e){
            throw new UserAlreadyExistsException();
        }catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @PutMapping("/track/addTrack")
    public ResponseEntity<?> addProductForUser(HttpServletRequest request, @RequestBody Track track) throws UserNotFoundException {
        ResponseEntity responseEntity = null;
        try{
            Claims claims = (Claims) request.getAttribute("claims");
            String userId = claims.getSubject();
            System.out.println("user Id from claims :: " + claims.getSubject());
            responseEntity = new ResponseEntity<>(userService.addTrackForUser(userId,track), HttpStatus.CREATED);
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @DeleteMapping("/track/deleteTrack/{trackId}")
    public ResponseEntity<?> deleteProductForUser(@PathVariable int trackId, HttpServletRequest request) throws TrackNotFoundException, UserNotFoundException {
        ResponseEntity responseEntity = null;
        try{
            Claims claims = (Claims) request.getAttribute("claims");
            String userId = claims.getSubject();
            System.out.println("user Id from claims :: " + claims.getSubject());
            responseEntity = new ResponseEntity<>(userService.deleteTrackFromUser(userId, trackId), HttpStatus.OK);
        }catch (TrackNotFoundException e){
            throw new TrackNotFoundException();
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        } catch(Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @GetMapping("/track/tracks")
    public ResponseEntity<?> getProductsForUser(HttpServletRequest request) throws UserNotFoundException {
        ResponseEntity responseEntity = null;
        try{
            Claims claims = (Claims) request.getAttribute("claims");
            String userId = claims.getSubject();
            System.out.println("user Id from claims :: " + claims.getSubject());
            responseEntity = new ResponseEntity<>(userService.getTrackForUser(userId), HttpStatus.OK);
        }catch(UserNotFoundException e){
            throw new UserNotFoundException();
        }
        catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @PutMapping("/track/updateTrack")
    public ResponseEntity<?> updateTrackForUser(HttpServletRequest request,@RequestBody Track track) throws UserNotFoundException {
        ResponseEntity responseEntity = null;
        try{
            Claims claims = (Claims) request.getAttribute("claims");
            String userId = claims.getSubject();
            System.out.println("user Id from claims :: " + claims.getSubject());
            responseEntity = new ResponseEntity<>(userService.updateTrackForUser(userId,track), HttpStatus.CREATED);
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
