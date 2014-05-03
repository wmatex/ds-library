/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna;

/**
 *
 * @author tomanlu2
 */
public class User {
    private final String name;
    private final String email;
    private final int type;
    
    public User(String name, String email, int type) {
        this.name = name;
        this.email = email;
        this.type = type;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getType() {
        return type;
    }
    
    

}
