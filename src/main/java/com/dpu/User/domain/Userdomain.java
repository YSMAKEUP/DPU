package com.dpu.User.domain;
import jakarta.persistence.Entity;


@Entity
public class Userdomain {


    @id
    private Long id;

    private String name;

    private String email;




    //가져오기, 보내기 --->get,set
    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public  void setEmail(String email){
        this.email = email;
    }

}
