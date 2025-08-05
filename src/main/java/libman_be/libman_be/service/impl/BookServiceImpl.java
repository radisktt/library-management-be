package libman_be.libman_be.service.impl;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.BookDTO;
import libman_be.libman_be.dto.response.BookResponseDTO;
import libman_be.libman_be.entity.*;
import libman_be.libman_be.exception.*;
import libman_be.libman_be.mapper.BookMapper;
import libman_be.libman_be.repository.*;
import libman_be.libman_be.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.catalog.CatalogException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookMapper bookMapper;


    @Override
    @Transactional
    public BaseResponse<BookDTO> create(BookDTO request) {
        Optional<Book> existing = bookRepository.findByTitle(request.getTitle());
        if (existing.isPresent()) {
            throw new BookException.BookTitleAlreadyExistsException("Book with title '" + request.getTitle() + "' already exists");
        }
        List<Category> categories = categoryRepository.findAllById(request.getCategoriesId());
        if (categories.size() != request.getCategoriesId().size()) {
            throw new CategoryException.CategoryNotFoundException("One or more category IDs do not exist");
        }
        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new PublisherException.PublisherNotFoundException("Publisher with ID '" + request.getPublisherId() + "' not found"));
        Set<Author> authors = new HashSet<>(authorRepository.findAllById(request.getAuthorIds()));
        if (authors.size() != request.getAuthorIds().size()) {
            throw new AuthorException.AuthorNotFoundException("One or more author IDs do not exist");
        }
        Library library = libraryRepository.findById(request.getLibraryId())
                .orElseThrow(() -> new LibraryException.LibraryNotFoundException("Library with ID '" + request.getLibraryId() + "' not found"));
        Book book = bookMapper.toEntity(request);
        book.setPublisher(publisher);
        book.setCategories(categories);
        book.setAuthors(authors);
        book.setLibrary(library);
        Book savedBook = bookRepository.save(book);

        BookDTO response = bookMapper.toDTO(savedBook);

        return BaseResponse.<BookDTO>builder()
                .status("success")
                .message("Book created successfully")
                .data(response)
                .build();
    }

    @Override
    @Transactional
    public BaseResponse<BookResponseDTO> update(Long id, BookDTO request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookException.BookNotFoundException("Book with ID '" + id + "' not found"));

        // check title exist
        Optional<Book> existing = bookRepository.findByTitle(request.getTitle());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new BookException.BookTitleAlreadyExistsException("Book with title '" + request.getTitle() + "' already exists");
        }

        List<Category> categories = categoryRepository.findAllById(request.getCategoriesId());
        if (categories.size() != request.getCategoriesId().size()) {
            throw new CategoryException.CategoryNotFoundException("One or more category IDs do not exist");
        }
        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new PublisherException.PublisherNotFoundException("Publisher with ID '" + request.getPublisherId() + "' not found"));
        Set<Author> authors = new HashSet<>(authorRepository.findAllById(request.getAuthorIds()));
        if (authors.size() != request.getAuthorIds().size()) {
            throw new AuthorException.AuthorNotFoundException("One or more author IDs do not exist");
        }
        Library library = libraryRepository.findById(request.getLibraryId())
                .orElseThrow(() -> new LibraryException.LibraryNotFoundException("Library with ID '" + request.getLibraryId() + "' not found"));

        // Update book
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPublicationDate(request.getPublicationDate());
        book.setPageCount(request.getPageCount());
        book.setQuantity(request.getQuantity());
        book.setPublisher(publisher);
        book.setCategories(categories);
        book.setAuthors(authors);
        book.setLibrary(library);

        Book updatedBook = bookRepository.save(book);
        BookResponseDTO response = bookMapper.toResponseDTO(updatedBook);

        return BaseResponse.<BookResponseDTO>builder()
                .status("success")
                .message("Book updated successfully")
                .data(response)
                .build();
    }

    @Override
    @Transactional
    public BaseResponse<String> delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookException.BookNotFoundException("Book with ID '" + id + "' not found"));
        bookRepository.deleteById(id);
        return BaseResponse.<String>builder()
                .status("success")
                .message("Book deleted successfully")
                .data("Book with ID '" + id + "' has been deleted")
                .build();
    }

    @Override
    public BaseResponse<BookResponseDTO> getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookException.BookNotFoundException("Book with ID '" + id + "' not found"));
        BookResponseDTO response = bookMapper.toResponseDTO(book);
        return BaseResponse.<BookResponseDTO>builder()
                .status("success")
                .message("Book retrieved successfully")
                .data(response)
                .build();
    }

    @Override
    public BaseResponse<List<BookResponseDTO>> getAll() {
        List<Book> books = bookRepository.findAll();
        List<BookResponseDTO> bookResponseDTOs = books.stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
        return BaseResponse.<List<BookResponseDTO>>builder()
                .status("success")
                .message("Books retrieved successfully")
                .data(bookResponseDTOs)
                .build();
    }
}
