package org.sid.cinema.service;

import org.sid.cinema.dao.*;
import org.sid.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService{

    @Autowired
    private VilleRpository villeRepository;
    @Autowired
    private CinemaRpository cinemaRepository;
    @Autowired
    private SalleRpository salleRepository;
    @Autowired
    private PlaceRpository placeRepository;
    @Autowired
    private SeanceRpository seanceRepository;
    @Autowired
    private FilmRpository filmRepository;
    @Autowired
    private ProjectionRpository projectionRepository;
    @Autowired
    private CategoryRpository categoryRepository;
    @Autowired
    private TicketRpository ticketRepository;

    @Override
    public void initVilles() {
        Stream.of("Casablanca", "Marrakech", "Youssoufia", "Rabat").forEach(v -> {
            Ville ville = new Ville();
            ville.setName(v);
            villeRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(ville -> {
            Stream.of("MegaRAMA", "IMAX", "FOUNOUN", "CHAHRAZAD").forEach(cinemaName -> {
                Cinema cinema = new Cinema();
                cinema.setName(cinemaName);
                cinema.setVille(ville);
                cinema.setNombreSalles(3 + (int) (Math.random() * 7));
                cinemaRepository.save(cinema);
            });
        });
    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema -> {
            for(int i=0; i < cinema.getNombreSalles(); i++){
                Salle salle = new Salle();
                salle.setName("Salle " + (i+1));
                salle.setCinema(cinema);
                salle.setNombrePlace(15 +(int) (Math.random() * 20));
                salleRepository.save(salle);
            }
        });
    }

    @Override
    public void initSeances() {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Stream.of("12:00", "15:00", "17:00", "19:00", "21:00").forEach(s -> {
            Seance seance = new Seance();
            try {
                seance.setHeureDebut(df.parse(s));
                seanceRepository.save(seance);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle -> {
            for (int i=0; i < salle.getNombrePlace(); i++){
                Place place = new Place();
                place.setNumero(i+1);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });
    }

    @Override
    public void initFilms() {
        double[] durres = new double[] {1, 1.5, 2, 2.5, 3};
        List<Categorie> categories = categoryRepository.findAll();
        Stream.of("Game of Thrones", "Seigneur des anneaux", "IRON Man", "Spiderman","cat women","forrest gump","Grren Book").forEach(filmTitre -> {
            Film film = new Film();
            film.setTitre(filmTitre);
            film.setDuree(durres[new Random().nextInt(durres.length)]);
            film.setPhoto(filmTitre.replaceAll("", "")+".jpg");
            film.setCategorie(categories.get(new Random().nextInt(categories.size())));
            Film save = filmRepository.save(film);
        });
    }

    @Override
    public void initProjections() {
        double[] prices = new double[] {30, 40, 50, 60, 70, 80};
        List<Film> films = filmRepository.findAll();
        villeRepository.findAll().forEach(ville -> {
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle -> {
                    int index = new Random().nextInt(films.size());
                    Film film = films.get(index);
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection = new Projection();
                            projection.setDateProjection(new Date());
                            projection.setFilm(film);
                            projection.setPrix(prices[new Random().nextInt(prices.length)]);
                            projection.setSalle(salle);
                            projection.setSeance(seance);
                            projectionRepository.save(projection);
                        });
                });
            });
        });
    }

    @Override
    public void initTickets() {
        projectionRepository.findAll().forEach(projection -> {
            projection.getSalle().getPlaces().forEach(place -> {
                Ticket ticket = new Ticket();
                ticket.setPlace(place);
                ticket.setPrix(projection.getPrix());
                ticket.setProjection(projection);
                ticket.setReserve(false);
                ticketRepository.save(ticket);
            });
        });
    }

    @Override
    public void initCategories() {
        Stream.of("Action", "Fiction", "Drama", "Histoire").forEach(cat -> {
            Categorie categorie = new Categorie();
            categorie.setName(cat);
            categoryRepository.save(categorie);
        });
    }
}