package libman_be.libman_be.mapper;

import libman_be.libman_be.dto.PublisherDTO;
import libman_be.libman_be.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {
    public PublisherDTO toDTO(Publisher publisher) {
        PublisherDTO dto = new PublisherDTO();
        dto.setId(publisher.getId());
        dto.setName(publisher.getName());
        dto.setAddress(publisher.getAddress());
        return dto;
    }

    public Publisher toEntity(PublisherDTO dto) {
        Publisher publisher = new Publisher();
        publisher.setId(dto.getId());
        publisher.setName(dto.getName());
        publisher.setAddress(dto.getAddress());
        return publisher;
    }
}
