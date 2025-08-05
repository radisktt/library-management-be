package libman_be.libman_be.mapper;

import libman_be.libman_be.dto.*;
import libman_be.libman_be.dto.response.BookResponseDTO;
import libman_be.libman_be.entity.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setPageCount(book.getPageCount());
        dto.setQuantity(book.getQuantity());

        if (book.getCategories() != null) {
            dto.setCategoriesId(
                    book.getCategories().stream()
                            .map(Category::getId)
                            .collect(Collectors.toList())
            );
        }

        if (book.getPublisher() != null) {
            dto.setPublisherId(book.getPublisher().getId());
        }

        if (book.getLibrary() != null) {
            dto.setLibraryId(book.getLibrary().getId());
        }

        if (book.getAuthors() != null) {
            dto.setAuthorIds(
                    book.getAuthors().stream()
                            .map(Author::getId)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    public Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationDate(dto.getPublicationDate());
        book.setPageCount(dto.getPageCount());
        book.setQuantity(dto.getQuantity());

        return book;
    }
    public BookResponseDTO toResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setPageCount(book.getPageCount());
        dto.setQuantity(book.getQuantity());

        if (book.getCategories() != null) {
            dto.setCategories(
                    book.getCategories().stream()
                            .map(category -> new CategoryDTO(category.getId(),category.getName()))
                            .collect(Collectors.toList())
            );
        }
        if (book.getPublisher() != null) {
            dto.setPublisher(new PublisherDTO(book.getPublisher().getId(), book.getPublisher().getName(),book.getPublisher().getAddress()));
        }
        if (book.getAuthors() != null) {
            dto.setAuthors(
                    book.getAuthors().stream()
                            .map(author -> new AuthorDTO(author.getId(),author.getName())) // Giả sử AuthorDTO có các trường id, name
                            .collect(Collectors.toSet())
            );
        }
        if (book.getLibrary() != null) {
            dto.setLibrary(new LibraryDTO(book.getLibrary().getId(),book.getLibrary().getName(),book.getLibrary().getLocation()));
        }
        return dto;
    }
}
