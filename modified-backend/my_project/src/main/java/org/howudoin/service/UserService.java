package org.howudoin.service;

import org.howudoin.model.User;
import org.howudoin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //should check if email exists
    public void registerUser(User user){
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already in use");
        }


        userRepository.save(user);
        System.out.println("User Saved");
    }

    //should check if email exists
    public User loginUser(String email, String password){

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User does not exist");
        }
        else{
            if(!password.matches(user.getPassword())){
                throw new RuntimeException("Invalid password");
            }
        }

        return user;
    }

    public List<User> showAllUsers(){
        return userRepository.findAll();
    }

    //send friend request
    public String sendRequest(String senderEmail, String receiverEmail){
        if (userRepository.findByEmail(senderEmail) == null) {
            throw new RuntimeException("Can't find email");
        }
        if (userRepository.findByEmail(receiverEmail) == null) {
            throw new RuntimeException("Can't find email");
        }

        User receiver = userRepository.findByEmail(receiverEmail);

        if((receiver.getFriendRequests() != null) && receiver.getFriendRequests().contains(senderEmail)){
            return "Request already sent";
        }

        receiver.setFriendRequests(senderEmail);
        userRepository.save(receiver);

        return "Friend request sent successfully to " + receiver.getName();
    }

    public String acceptRequest(String senderEmail, String receiverEmail){
        if (userRepository.findByEmail(senderEmail) == null) {
            throw new RuntimeException("Can't find email");
        }
        if (userRepository.findByEmail(receiverEmail) == null) {
            throw new RuntimeException("Can't find email");
        }

        User sender = userRepository.findByEmail(senderEmail);
        User receiver = userRepository.findByEmail(receiverEmail);


        if(receiver.getFriendRequests().contains(senderEmail)){
            receiver.getFriendRequests().remove(senderEmail);

            receiver.getFriends().add(senderEmail);
            sender.getFriends().add(receiverEmail);

            userRepository.save(sender);
            userRepository.save(receiver);

            return "Friend request accepted";
        }

        return "No such friend request found";

    }

    public List<String> showFriends(String email){
        if (userRepository.findByEmail(email) == null) {
            throw new RuntimeException("Can't find email");
        }

        User user = userRepository.findByEmail(email);

        return user.getFriends();
    }

    public List<String> showFriendRequests(String email){
        if (userRepository.findByEmail(email) == null) {
            throw new RuntimeException("Can't find email");
        }

        User user = userRepository.findByEmail(email);

        return user.getFriendRequests();
    }

}
