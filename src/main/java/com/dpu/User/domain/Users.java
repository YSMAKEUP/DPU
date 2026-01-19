package com.dpu.User.domain;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;


@Entity
@Table(name = "users")
public class Users {
     //권한

    @Id  //기본 키
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 키를 증가시킨다.
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;









    //가져오기, 보내기 --->getter,setter

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
