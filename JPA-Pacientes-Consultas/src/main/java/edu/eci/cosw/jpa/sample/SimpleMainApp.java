/*
 * Copyright (C) 2016 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.cosw.jpa.sample;

import edu.eci.cosw.jpa.sample.model.*;
import java.util.Calendar;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author hcadavid
 */
public class SimpleMainApp {

    public static void main(String a[]) {
        SessionFactory sf = getSessionFactory();
        Session s = sf.openSession();
        Transaction tx = s.beginTransaction();

        Paciente pacient = (Paciente) s.load(Paciente.class, new PacienteId(3, "cc"));
        System.out.println("id:"+pacient.getId()+" Nombre: "+pacient.getNombre()+" consultas: "+pacient.getConsultas().toString());
        System.out.println("ºººººººººººººººººººººººººººººººººººººººººººººººººººº");
        try {
            for (Consulta c : pacient.getConsultas()) {
                System.out.println(c.getResumen());
                System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
            }
        } catch (Exception e) {
            System.out.println("El usuario no tiene Consultas asignadas.");
        }
        Consulta newConsult =new Consulta(new java.sql.Date(Calendar.getInstance().getTime().getTime()), "Descripcion: "+pacient.getNombre());
        pacient.getConsultas().add(newConsult);
        s.saveOrUpdate(pacient);
        tx.commit();
        s.close();
        sf.close();

    }

    public static SessionFactory getSessionFactory() {
        // loads configuration and mappings
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry
                = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

        // builds a session factory from the service registry
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

}
