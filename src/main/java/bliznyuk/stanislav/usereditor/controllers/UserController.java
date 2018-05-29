package bliznyuk.stanislav.usereditor.controllers;

import bliznyuk.stanislav.usereditor.exception.InvalidDataException;
import bliznyuk.stanislav.usereditor.exception.ResourceNotFoundException;
import bliznyuk.stanislav.usereditor.entities.User;
import bliznyuk.stanislav.usereditor.repository.UserRepository;
import org.apache.catalina.connector.Response;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.plugin.dom.exception.InvalidAccessException;

import javax.validation.Valid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api")
public class UserController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;

    //Список всех пользователей
    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.info("TEST");
        return userRepository.findAll();
    }

    //Добавить пользователя
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    //Один пользователь
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable(value = "id") Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
    }

    //Редактировать пользователя
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) throws InvalidDataException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        if(userDetails.getName().length()>255){
            throw new InvalidDataException(" имя не должно превышать 255 символов");

        }
        if(userDetails.getSecondName().length()>255){
            throw new InvalidDataException("отчество не должно превышать 255 символов");
        }
        if(userDetails.getSurname().length()>255){
            throw new InvalidDataException("фамилия не должна превышать 255 символов");
        }

        user.setName(userDetails.getName());
        user.setSecondName(userDetails.getSecondName());
        user.setSurname(userDetails.getSurname());
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(userDetails.getBirthday());
        } catch (Exception ex) {

            throw new InvalidDataException("дата рождения должна быть в формате yyyy-MM-dd");

        }

        User updatedUser = userRepository.save(user);
        return updatedUser;
    }

    //Удалить пользователя
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }

    //Один пользователь по имени
    @GetMapping("/username/{name}")
    public List<User> getUserByName(@PathVariable(value = "name") String name) {

        List<User> user =  userRepository.findByName(name);
        if(user.size() == 0) {
            throw new ResourceNotFoundException("User", "name", name);
        } else {
            return user;
        }

    }

    //Один пользователь по фамилии
    @GetMapping("/users/{surname}")
    public List<User> getUserById(@PathVariable(value = "id") String surname) {

        List<User> user = userRepository.findBySurname(surname);
        if(user.size() == 0) {
            throw new ResourceNotFoundException("User", "surname", surname);
        } else {
            return user;
        }

    }

    //Пользователи родившиеся позже указанной даты
    @GetMapping("/later/{date}")
    public List<User> getUserLater(@PathVariable(value = "date") String date) throws InvalidDataException {

        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception ex) {
            throw new InvalidDataException("дата  должна быть в формате yyyy-MM-dd");
        }

        List<User> user =  userRepository.dateLater(date);
        if(user == null) {
            throw new ResourceNotFoundException("User", "birthday", ">"+date);
        } else {
            return user;
        }

    }

    //Пользователи родившиеся раньше указанной даты
    @GetMapping("/earlier/{date}")
    public List<User> getUserEarlier(@PathVariable(value = "date") String date) throws InvalidDataException {

        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception ex) {
            throw new InvalidDataException("дата  должна быть в формате yyyy-MM-dd");
        }

        List<User> user =  userRepository.dateEarlier(date);
        if(user == null) {
            throw new ResourceNotFoundException("User", "birthday", "<"+date);
        } else {
            return user;
        }

    }



    //Добавить друга
    @PutMapping("/useraddfriend/{id}")
    public User addFriend(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        User friend = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userDetails.getId()));

        user.addFriend(friend);
        friend.addFriend(user);

        User updatedUser = userRepository.save(user);
        userRepository.save(friend);
        return updatedUser;
    }

    //Удалить друга
    @PutMapping("/userdeletefriend/{id}")
    public User deleteFriend(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        User friend = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userDetails.getId()));

        user.deleteFriend(friend);
        friend.deleteFriend(user);
        User updatedUser = userRepository.save(user);
        userRepository.save(friend);
        return updatedUser;
    }

    //Цепочка друзей
    @PostMapping("/friendschain/{id}")
    public List<User> getFriendsChain(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) {


        User user = userRepository.getOne(userId);
        User friend = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userDetails.getId()));

        List<User> us = Searcher.find(user,friend);

        return us;


    }


}
