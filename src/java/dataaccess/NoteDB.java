package dataaccess;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import models.Note;
import models.User;

public class NoteDB {

    public List<Note> getAll() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            List<Note> allNotes = em.createNamedQuery("Note.findAll", Note.class).getResultList();
            return allNotes;
        } finally {
            em.close();
        }
    }

    public List<Note> getAllByOwner(String owner) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            User user = em.find(User.class,
                     owner);
            return user.getNoteList();
        } finally {
            em.close();
        }
    }
    
    public Note get(int id){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            Note note = em.find(Note.class,id);
            return note;
        } finally {
            em.close();
        }
    }
    
    public void update(Note note){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try{
            transaction.begin();
            em.merge(note);
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
        }
        finally {
            em.close();
        }
    }
    
    public void insert(Note note) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            User user = note.getOwner();
            user.getNoteList().add(note);
            transaction.begin();
            em.persist(note);
            em.merge(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
        }
    }

    public void delete(Note note){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try{
            User user = note.getOwner();
            user.getNoteList().remove(note);
            transaction.begin();
            em.remove( em.merge(note) );
            em.merge(user);
            transaction.commit();
        }
        catch (Exception e){
            transaction.rollback();
        }
        finally {
            em.close();
        }
    }
    
    
}
