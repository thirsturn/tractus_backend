package com.tractus.backend.config;

import com.tractus.backend.models.Space;
import com.tractus.backend.repositories.SpaceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner initDatabase(SpaceRepository spaceRepository) {
        return args -> {
            if (spaceRepository.count() == 0) {
                Space generalSpace = new Space();
                generalSpace.setName("General");
                generalSpace.setDescription("The main space for general discussions");
                spaceRepository.save(generalSpace);
                
                Space techSpace = new Space();
                techSpace.setName("Technology");
                techSpace.setDescription("Discussions about tech and coding");
                spaceRepository.save(techSpace);
                
                System.out.println("Default spaces seeded into the database!");
            }
        };
    }
}
