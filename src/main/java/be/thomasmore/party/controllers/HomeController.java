package be.thomasmore.party.controllers;

import be.thomasmore.party.model.Artist;
import be.thomasmore.party.model.Party;
import be.thomasmore.party.model.PartyAnimal;
import be.thomasmore.party.model.Venue;
import be.thomasmore.party.repositories.ArtistRepository;
import be.thomasmore.party.repositories.PartyAnimalRepository;
import be.thomasmore.party.repositories.PartyRepository;
import be.thomasmore.party.repositories.VenueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
public class HomeController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private PartyAnimalRepository partyAnimalRepository;

    private final String applicationName = "It's Party Time!!";

    @GetMapping({"/", "/parties"})
    public String parties(Model model) {
        model.addAttribute("appName", applicationName);
        model.addAttribute("parties", partyRepository.findAll());
        return "parties";
    }

    @GetMapping({"/party/{partyId}"})
    public String party(@PathVariable int partyId, Model model) {
        model.addAttribute("appName", applicationName);
        addPartyInModel(model, partyRepository.findById(partyId));
        return "party";
    }

    @GetMapping({"/party"})
    public String party(Model model) {
        model.addAttribute("appName", applicationName);
        model.addAttribute("venueName", null);
        model.addAttribute("venue", null);
        return "party";
    }

    private void addPartyInModel(Model model, Optional<Party> optionalPartyFromDb) {
        long nrOfVenues = partyRepository.count();
        if (optionalPartyFromDb.isPresent()) {
            Party party = optionalPartyFromDb.get();
            int partyId = party.getId();
            model.addAttribute("party", party);
            model.addAttribute("idOfPrevParty", partyId > 0 ? partyId - 1 : nrOfVenues - 1);
            model.addAttribute("idOfNextParty", partyId < nrOfVenues - 1 ? partyId + 1 : 0);
        } else {
            model.addAttribute("partyId", null);
        }
    }

    @GetMapping({"/venues", "/venues/{size}"})
    public String venues(@PathVariable(required = false) String size,
                         @RequestParam(required = false) Integer minNrOfPersons,
                         @RequestParam(required = false) Integer maxNrOfPersons,
                         @RequestParam(required = false) String venueNameSearchString,
                         Model model) {
        logger.info("Here we are!!!" );
        logger.info(String.format("params: min = %d, max=%d, venueName=%s", minNrOfPersons, maxNrOfPersons, venueNameSearchString));

        model.addAttribute("appName", applicationName);
        model.addAttribute("filterButtons", new String[]{"all", "S", "M", "L", "XL"});

        if (minNrOfPersons == null && maxNrOfPersons == null && size != null) {
            switch (size) {
                case "S":
                    minNrOfPersons = null;
                    maxNrOfPersons = 100;
                    break;
                case "M":
                    minNrOfPersons = 100;
                    maxNrOfPersons = 300;
                    break;
                case "L":
                    minNrOfPersons = 300;
                    maxNrOfPersons = 500;
                    break;
                case "XL":
                    minNrOfPersons = 500;
                    maxNrOfPersons = null;
                    break;
            }
        }
        logger.info(String.format("interpreted: min = %d, max=%d, venueName=%s", minNrOfPersons, maxNrOfPersons, venueNameSearchString));
        model.addAttribute("venues", venueRepository.findVenuesBySearchCriteria(minNrOfPersons, maxNrOfPersons, venueNameSearchString));
        model.addAttribute("minNrOfPersons", minNrOfPersons);
        model.addAttribute("maxNrOfPersons", maxNrOfPersons);
        model.addAttribute("venueNameSearchString", venueNameSearchString);
        model.addAttribute("size", size);
        return "venues";
    }

    @GetMapping({"/venue"})
    public String venue(Model model) {
        model.addAttribute("appName", applicationName);
        model.addAttribute("venueName", null);
        model.addAttribute("venue", null);
        return "venue";
    }

    @GetMapping({"/venue/{venueId}"})
    public String venue(@PathVariable int venueId, Model model) {
        model.addAttribute("appName", applicationName);
        Optional<Venue> optionalVenueFromDb = venueRepository.findById(venueId);
        long nrOfVenues = venueRepository.count();
        if (optionalVenueFromDb.isEmpty()) {
            model.addAttribute("venue", null);
            model.addAttribute("parties", new Party[]{});
        } else {
            Venue venue = optionalVenueFromDb.get();
            model.addAttribute("venue", venue);
            model.addAttribute("idOfPrevVenue", venueId > 0 ? venueId - 1 : nrOfVenues - 1);
            model.addAttribute("idOfNextVenue", venueId < nrOfVenues - 1 ? venueId + 1 : 0);
            model.addAttribute("parties", partyRepository.findPartiesByVenue(venue));
        }
        return "venue";
    }

    @GetMapping({"/artists"})
    public String artists(Model model) {
        model.addAttribute("appName", applicationName);
        model.addAttribute("artists", artistRepository.findAll());
        return "artists";
    }

    @GetMapping({"/artist/{artistId}"})
    public String artist(@PathVariable int artistId, Model model) {
        model.addAttribute("appName", applicationName);
        Optional<Artist> optionalArtistFromDb = artistRepository.findById(artistId);
        addArtistInModel(artistId, model, optionalArtistFromDb);
        return "artist";
    }

    private void addArtistInModel(@PathVariable int artistId, Model model, Optional<Artist> optionalArtistFromDb) {
        if (optionalArtistFromDb.isPresent()) {
            long nrOfArtists = artistRepository.count();
            model.addAttribute("artist", optionalArtistFromDb.get());
            model.addAttribute("idOfPrevArtist", artistId > 0 ? artistId - 1 : nrOfArtists - 1);
            model.addAttribute("idOfNextArtist", artistId < nrOfArtists - 1 ? artistId + 1 : 0);
        } else {
            model.addAttribute("artist", null);
        }
    }


    @GetMapping({"/animal/{animalId}"})
    public String animal(@PathVariable int animalId, Model model) {
        model.addAttribute("appName", applicationName);
        Optional<PartyAnimal> optionalAnimalFromDb = partyAnimalRepository.findById(animalId);
        if (optionalAnimalFromDb.isPresent()) {
            model.addAttribute("animal", optionalAnimalFromDb.get());
        } else {
            model.addAttribute("animal", null);
        }

        return "animal";
    }

}
