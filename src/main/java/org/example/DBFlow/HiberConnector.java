package org.example.DBFlow;

import org.example.model.Course;
import org.example.model.RepositoryCourses;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;


public class HiberConnector {

    private final SessionFactory sessionFactory;


    public HiberConnector() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }

    public void actions(String choice, int id, String... data) {
        Session session = getSession();
        session.beginTransaction();
        switch (choice) {
            case "1":
                createCourses(session);
                session.getTransaction().commit();
                break;
            case "2":
                showAllFromBase(session);
                break;
            case "3":
                findCourse(session, id);
                break;
            case "4":
                addNewCourse(session, data);
                session.getTransaction().commit();
                break;
            case "5":
                updateCourse(session, id, data);
                session.getTransaction().commit();
                break;
            case "6":
                deleteCourse(session, id);
                session.getTransaction().commit();
                break;

            default:
                break;
        }

    }

    private void createCourses(Session session) {
        for (int i = 0; i < 11; i++) {
            session.persist(RepositoryCourses.coursesCreator());
        }
        System.out.println("Добавлены новые курсы");
    }

    private void showAllFromBase(Session session) {
        List<Course> courses = session.createQuery("FROM Course ", Course.class).getResultList();
        courses.forEach(System.out::println);
    }

    private void findCourse(Session session, int id) {
        Course loadCourse = session.get(Course.class, id);
        System.out.println(loadCourse);
    }

    private void addNewCourse(Session session, String... data) {
        Course course = new Course(data[0], data[1]);
        session.persist(course);

    }

    private void updateCourse(Session session, int id, String... data) {
        Course course = session.get(Course.class, id);
        if (data[0].isEmpty() && data[1].isEmpty())
            return;
        else if (data[0].isEmpty()) course.setDuration(data[1]);
        else if (data[1].isEmpty()) course.setName(data[0]);
        else {
            course.setName(data[0]);
            course.setDuration(data[1]);
        }
        session.merge(course);

    }

    private void deleteCourse(Session session, int id) {
        Course course = session.get(Course.class, id);
        session.remove(course);

    }

}
