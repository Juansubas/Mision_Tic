package com.hibernate.introduction.controlador;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.hibernate.introduction.modelo.Mascota;

public class MascotaControlador {

  // ATRIBUTOS
  SessionFactory factory;

  public MascotaControlador() {
    // Crear objeto fabricante de sesiones
    factory = new Configuration()
        .configure("cfg.xml")
        .addAnnotatedClass(Mascota.class)
        .buildSessionFactory();
  }

  public Session createSession() {
    Session session = factory.openSession();
    session.beginTransaction();
    return session;
  }

  public void create(String nombre, String apellido, String tipo_mascota, String raza, int edad, String observacion)
      throws Exception {
    Session session = factory.openSession();
    session.beginTransaction();
    Mascota mascota = new Mascota(nombre, apellido, tipo_mascota, raza, edad, observacion);
    session.persist(mascota);
    session.getTransaction().commit();
    session.close();
  }

  public String readById(int id) throws Exception {
    Session session = factory.openSession();
    session.beginTransaction();
    Mascota mascota = session.find(Mascota.class, id);
    session.close();
    return mascota.toString();
  }

  public List<String> getByLastname(String apellido) throws Exception {
    // List<String> mascotas = new ArrayList<>();
    Session session = factory.openSession();
    session.beginTransaction();
    List<Mascota> objMascotas = session.createQuery("from Mascota where apellido = :ap", Mascota.class)
        .setParameter("ap", apellido).list();
    session.close();
    return objToString(objMascotas);
    // return mascotas;
  }

  public List<String> objToString(List<Mascota> objMascotas) {
    List<String> mascotas = new ArrayList<>();
    for (int i = 0; i < objMascotas.size(); i++) {
      mascotas.add(objMascotas.get(i).toString());
    }
    return mascotas;
  }

  public List<String> getList() throws Exception {
    Session session = factory.openSession();
    session.beginTransaction();
    List<Mascota> mascotas = session.createQuery("from Mascota", Mascota.class).list();
    session.close();
    return objToString(mascotas);
  }

  public void update(int id, String nombre, String apellido, String tipo_mascota, String raza, int edad,
      String observacion) throws Exception {
    Session session = factory.openSession();
    session.beginTransaction();
    Mascota mascota = session.find(Mascota.class, id);
    // Actualizar objeto
    mascota.setNombre(nombre);
    mascota.setApellido(apellido);
    mascota.setTipo_mascota(tipo_mascota);
    mascota.setRaza(raza);
    mascota.setEdad(edad);
    mascota.setObservacion(observacion);
    // Realziar actualización en la BD
    session.merge(mascota);
    session.getTransaction().commit();
    session.close();
  }

  public void deleteService(Mascota mascota) {
    Session session = createSession();
    // Eliminar
    session.remove(mascota);
    session.getTransaction().commit();
  }

  public void delete(int id) {
    Session session = createSession();
    Mascota mascota = session.find(Mascota.class, id);
    deleteService(mascota);
  }

}