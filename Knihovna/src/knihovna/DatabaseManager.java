/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knihovna;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;
import knihovna.entity.Autor;
import knihovna.entity.Uzivatel;
import knihovna.entity.VwRezervace;
import knihovna.entity.VwTitul;
import knihovna.entity.VwVypujcka;
import knihovna.entity.Vytisk;

/**
 *
 * @author wmatex
 */
public class DatabaseManager {

    private static DatabaseManager mInstance = null;
    private final EntityManager mEm;
    private final EntityManagerFactory mEmf;
    private static final int PAGE_SIZE = 5;

    private DatabaseManager() {
        mEmf = Persistence.createEntityManagerFactory("KnihovnaPU");
        mEm = mEmf.createEntityManager();

    }

    public static void init() {
        if (DatabaseManager.mInstance == null) {
            DatabaseManager.mInstance = new DatabaseManager();
        }
    }

    public static void destroy() {
        if (DatabaseManager.mInstance != null) {
            DatabaseManager.mInstance.mEm.close();
            DatabaseManager.mInstance.mEmf.close();
        }
    }

    public static DatabaseManager getInstance() {
        return DatabaseManager.mInstance;
    }

    public Uzivatel login(String email, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String md5Password = md5(password);
        try {
            return mEm.createNamedQuery("Uzivatel.prihlasit", Uzivatel.class)
                    .setParameter("email", email)
                    .setParameter("heslo", md5Password)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Vytisk getPrintByBarcode(String barcode) {
        try {
            return mEm.createNamedQuery("Vytisk.findByCarKod", Vytisk.class)
                    .setParameter("carKod", barcode)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Uzivatel getUserByEmail(String email) {
        try {
            return mEm.createNamedQuery("Uzivatel.findByEmail", Uzivatel.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Collection<VwTitul> searchTitles(String criteria, int pageno) {
        criteria = criteria.trim();
        criteria = criteria.replaceAll("\\s+", "|");
        return mEm.createNamedQuery("VwTitul.searchForTitul", VwTitul.class)
                .setParameter(1, criteria)
                .setParameter(2, criteria)
                .setParameter(3, PAGE_SIZE)
                .setParameter(4, pageno * PAGE_SIZE)
                .getResultList();
    }

    public String createPrint(Integer idTitul) {
        StoredProcedureQuery query = mEm.createNamedStoredProcedureQuery("Vytisk.novy");
        query.setParameter("_printId", idTitul);
        String barcode = null;
        try {
            query.execute();
            barcode = (String) query.getOutputParameterValue(13);
            mEm.clear();
        } catch (PersistenceException e) {
            throw new EntityExistsException();
        }
        return barcode;
    }

    public String createUser(String name, String surname, String email) {
        StoredProcedureQuery query = mEm.createNamedStoredProcedureQuery("Uzivatel.novy");
        query.setParameter("_jmeno", name);
        query.setParameter("_prijmeni", surname);
        query.setParameter("_email", email);

        String password = null;
        try {
            query.execute();
            password = (String) query.getOutputParameterValue(4);
            mEm.clear();
        } catch (PersistenceException e) {
            throw new EntityExistsException();
        }
        return password;
    }

    public void changePassword(Uzivatel user, String password) {
        user.setHeslo(password);
        mEm.persist(user);
        mEm.clear();
    }

    public void createReservation(Uzivatel user, VwTitul title) {
        StoredProcedureQuery query = mEm.createNamedStoredProcedureQuery("VwRezervace.nova");
        query.setParameter("_id_uzivatel", user.getIdUzivatel());
        query.setParameter("_id_titul", title.getIdTitul());
        query.setParameter("_interval", null);

        query.execute();
        mEm.clear();
    }

    public void createBorrowing(Vytisk print, Uzivatel user) {
        VwVypujcka borrowing = new VwVypujcka(print.getIdVytisk(), user.getIdUzivatel());
        EntityTransaction transaction = mEm.getTransaction();
        try {
            transaction.begin();
            mEm.persist(borrowing);
            transaction.commit();
            mEm.clear();
        } catch (PersistenceException ex) {
            throw new EntityExistsException();
        }
    }

    public void returnBorrowing(VwVypujcka borrowing) {
        EntityTransaction transaction = mEm.getTransaction();
        transaction.begin();
        borrowing.setJeVraceno(true);
        mEm.persist(borrowing);
        transaction.commit();
        mEm.clear();
    }

    public List<VwRezervace> getWaitingRezervations(int pageno) {
        return mEm.createNamedQuery("VwRezervace.findWaiting", VwRezervace.class)
                .setParameter(1, PAGE_SIZE)
                .setParameter(2, pageno * PAGE_SIZE)
                .getResultList();
    }

    public List<Uzivatel> getUsers() {
        return mEm.createNamedQuery("Uzivatel.getAll", Uzivatel.class)
                .getResultList();
    }

    public List<VwVypujcka> getBorrowsOfUser(Uzivatel user, int pageno) {
        return mEm.createNamedQuery("Vypujcka.findByUser", VwVypujcka.class)
                .setParameter(1, user.getIdUzivatel())
                .setParameter(2, PAGE_SIZE)
                .setParameter(3, pageno * PAGE_SIZE)
                .getResultList();
    }

    public List<VwRezervace> getReservationsOfUser(Uzivatel user, int pageno) {
        return mEm.createNamedQuery("VwRezervace.findByUser", VwRezervace.class)
                .setParameter(1, user.getIdUzivatel())
                .setParameter(2, PAGE_SIZE)
                .setParameter(3, pageno * PAGE_SIZE)
                .getResultList();
    }

    public VwVypujcka getBorrowByVytisk(Vytisk print) {
        try {
            return mEm.createNamedQuery("Vypujcka.findByVytisk", VwVypujcka.class)
                    .setParameter("id", print.getIdVytisk())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }

    public void fullfillReservation(int id_titul) {
        StoredProcedureQuery query = mEm.createNamedStoredProcedureQuery("VwRezervace.spln_rezervaci");
        query.setParameter("_id_titul", id_titul);

        query.execute();
        mEm.clear();
    }

    public List<String> getGenres() {
        return mEm.createNamedQuery("VwTitul.getGenres", String.class)
            .getResultList();
    }

    public List<Autor> getAllAuthors() {
        return mEm.createNamedQuery("Autor.findAll", Autor.class)
            .getResultList();
    }

    public EntityTransaction getTransaction() {
        return mEm.getTransaction();
    }

    public int getGenreId(String genre) {
        return mEm.createNamedQuery("VwTitul.getGenreId", Integer.class)
            .setParameter(1, genre)
            .getSingleResult();
    }

    public void insertAuthor(int idTitle, String firstName, String lastName) {
        StoredProcedureQuery query = mEm.createNamedStoredProcedureQuery("Autor.vloz_autora");
        query.setParameter("_id_titul", idTitle);
        query.setParameter("_jmeno", firstName);
        query.setParameter("_prijmeni", lastName);

        query.execute();
        mEm.clear();
    }

    public void merge(Object o) {
        mEm.merge(o);
        mEm.flush();
    }

    public void persist(Object o) {
        mEm.persist(o);
        mEm.flush();
    }

    public void clear() {
        mEm.clear();
    }

    public void deleteAllBookAuthors(VwTitul book) {
        mEm.createNamedQuery("VwTitul.deleteJoins")
            .setParameter(1, book.getIdTitul())
            .executeUpdate();
    }

    public static String md5(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(string.getBytes("UTF-8"));
        byte[] bytes = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

}
