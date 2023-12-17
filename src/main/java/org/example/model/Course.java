package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {

    public static final String tableName = "course";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;
    @Column(name = "duration")
    private String duration;

    public Course(int id, String name, String duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public Course(String name, String duration) {
        this.name = name;
        this.duration = duration;
    }

    public Course() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Course{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", duration=" + duration +
               '}';
    }
}
