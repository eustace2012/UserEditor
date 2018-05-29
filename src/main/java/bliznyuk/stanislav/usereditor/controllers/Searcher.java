package bliznyuk.stanislav.usereditor.controllers;

import bliznyuk.stanislav.usereditor.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Searcher {

    private static List<User> used = new ArrayList();

    public static List<User> find (User u1, User u2){

        List<User> result = new ArrayList();
        Set<User> friends = u1.getFriends();
        result.add(u1);
        used.add(u1);

        for(User user:  friends){
            if(used.contains(user)) {
                continue;
            }
            if(user.equals(u2)){
                result.add(u2);
                System.out.println("ins " + u2.getId());
                return result;
            } else {
                result.addAll(find(user,u2));
                if(result.contains(u2)){
                    return result;
                }
            }

        }
        result.remove(u1);
        return result;

    }
}
