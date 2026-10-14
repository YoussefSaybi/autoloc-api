package tn.esprit.autoloc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.ModePaiement;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.ContratRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class AutolocApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutolocApiApplication.class, args);
    }

    @Bean
    CommandLineRunner test(ContratRepository contratRepo) {
        return args -> {
            Contrat c = new Contrat();
            c.setDateSignature(LocalDate.now());
            c.setMontantTotal(new BigDecimal("500"));
            c.setValide(true);

            Paiement p1 = new Paiement();
            p1.setMontant(new BigDecimal("200"));
            p1.setDatePaiement(LocalDate.now());
            p1.setModePaiement(ModePaiement.CARTE);

            c.addPaiement(p1);
            contratRepo.save(c);   // p1 is saved thanks to the cascade
        };
    }
}