package me.mneri.offer.mapping;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application wide configuration for object mapping.
 *
 * @author mneri
 */
@Configuration
public class MappingConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setProvider(new CustomProvider());
        return modelMapper;
    }
}
