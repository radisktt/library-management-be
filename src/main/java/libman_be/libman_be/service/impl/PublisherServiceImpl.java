package libman_be.libman_be.service.impl;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.PublisherDTO;
import libman_be.libman_be.entity.Publisher;
import libman_be.libman_be.mapper.PublisherMapper;
import libman_be.libman_be.repository.PublisherRepository;
import libman_be.libman_be.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    private final PublisherMapper publisherMapper;

    public PublisherServiceImpl(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    @Override
    public BaseResponse<PublisherDTO> create(PublisherDTO dto) {
        if (publisherRepository.findByName(dto.getName()) != null) {
            return BaseResponse.<PublisherDTO>builder()
                    .status("fail")
                    .message("Publisher with name " + dto.getName() + " already exists")
                    .build();
        }
        Publisher publisher = publisherMapper.toEntity(dto);
        Publisher saved = publisherRepository.save(publisher);
        return BaseResponse.<PublisherDTO>builder()
                .status("success")
                .message("Publisher created successfully")
                .data(publisherMapper.toDTO(saved))
                .build();
    }

    @Override
    public BaseResponse<PublisherDTO> update(Long id, PublisherDTO dto) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher with id " + id + " not found"));

        if (dto.getName() != null && !dto.getName().equals(publisher.getName())) {
            Publisher existed = publisherRepository.findByName(dto.getName());
            if (existed != null) {
                return BaseResponse.<PublisherDTO>builder()
                        .status("fail")
                        .message("Publisher with name " + dto.getName() + " already exists")
                        .build();
            }
            publisher.setName(dto.getName());
        }

        if (dto.getAddress() != null) {
            publisher.setAddress(dto.getAddress());
        }

        Publisher updated = publisherRepository.save(publisher);
        return BaseResponse.<PublisherDTO>builder()
                .status("success")
                .message("Publisher updated successfully")
                .data(publisherMapper.toDTO(updated))
                .build();
    }

    @Override
    public BaseResponse<String> delete(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher with id " + id + " not found"));
        publisherRepository.delete(publisher);
        return BaseResponse.<String>builder()
                .status("success")
                .message("Publisher deleted successfully")
                .data("Publisher with ID " + id + " has been deleted")
                .build();
    }

    @Override
    public BaseResponse<PublisherDTO> getById(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher with id " + id + " not found"));
        return BaseResponse.<PublisherDTO>builder()
                .status("success")
                .message("Publisher retrieved successfully")
                .data(publisherMapper.toDTO(publisher))
                .build();
    }

    @Override
    public BaseResponse<List<PublisherDTO>> getAll() {
        List<Publisher> publishers = publisherRepository.findAll();
        return BaseResponse.<List<PublisherDTO>>builder()
                .status("success")
                .message("All publishers retrieved successfully")
                .data(publishers.stream().map(publisherMapper::toDTO).collect(Collectors.toList()))
                .build();
    }
}
