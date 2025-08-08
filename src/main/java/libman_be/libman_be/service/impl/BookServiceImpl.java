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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.catalog.CatalogException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    private final PublisherRepository publisherRepository;

    private final AuthorRepository authorRepository;

    private final LibraryRepository libraryRepository;

    private final BookMapper bookMapper;

    public BookServiceImpl(
            BookRepository bookRepository, CategoryRepository categoryRepository,
            PublisherRepository publisherRepository, AuthorRepository authorRepository,
            LibraryRepository libraryRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.libraryRepository = libraryRepository;
        this.bookMapper = bookMapper;
    }

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
    public BaseResponse<Page<BookResponseDTO>> getAll(Pageable pageable) {
        Page<Book> books = bookRepository.findAllBook(pageable);
        Page<BookResponseDTO> bookResponseDTOs = books.map(book -> bookMapper.toResponseDTO(book));

        return BaseResponse.<Page<BookResponseDTO>>builder()
                .status("success")
                .message("Books retrieved successfully")
                .data(bookResponseDTOs)
                .build();
    }

    @Override
    public BaseResponse<Page<BookResponseDTO>> findByAuthorId(Long id, Pageable pageable) {
        authorRepository.findById(id)
                .orElseThrow(() -> new AuthorException.AuthorNotFoundException("Author with ID '" + id + "' not found"));
        Page<Book> books = bookRepository.findByAuthorId(id, pageable);
        Page<BookResponseDTO> responseDTOS = books.map(book -> bookMapper.toResponseDTO(book));

        return BaseResponse.<Page<BookResponseDTO>>builder()
                .status("success")
                .message("Books by author id "+ id +" retrieved successfully")
                .data(responseDTOS)
                .build();
    }

    @Override
    public BaseResponse<Page<BookResponseDTO>> findByAuthorName(String name, Pageable pageable){
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty");
        }
        Page<Book> books = bookRepository.findByAuthorName(name, pageable);
        Page<BookResponseDTO> response = books.map(bookMapper::toResponseDTO);
        return BaseResponse.<Page<BookResponseDTO>>builder()
                .status("success")
                .message("Books by author name retrieved successfully")
                .data(response)
                .build();
    }

    @Override
    public BaseResponse<Page<BookResponseDTO>> findByCategoryId(Long categoryId, Pageable pageable) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException.CategoryNotFoundException("Category with ID '" + categoryId + "' not found"));
        Page<Book> books = bookRepository.findByCategoryId(categoryId, pageable);
        Page<BookResponseDTO> response = books.map(bookMapper::toResponseDTO);
        return BaseResponse.<Page<BookResponseDTO>>builder()
                .status("success")
                .message("Books by category retrieved successfully")
                .data(response)
                .build();
    }

    @Override
    public BaseResponse<Page<BookResponseDTO>> findByCategoryName(String categoryName, Pageable pageable) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        Page<Book> books = bookRepository.findByCategoryName(categoryName, pageable);
        Page<BookResponseDTO> response = books.map(bookMapper::toResponseDTO);
        return BaseResponse.<Page<BookResponseDTO>>builder()
                .status("success")
                .message("Books by category name retrieved successfully")
                .data(response)
                .build();
    }

    @Override
    public BaseResponse<Page<BookResponseDTO>> findByPublisherId(Long publisherId, Pageable pageable) {
        publisherRepository.findById(publisherId)
                .orElseThrow(() -> new PublisherException.PublisherNotFoundException("Publisher with ID '" + publisherId + "' not found"));
        Page<Book> books = bookRepository.findByPublisherId(publisherId, pageable);
        Page<BookResponseDTO> response = books.map(bookMapper::toResponseDTO);
        return BaseResponse.<Page<BookResponseDTO>>builder()
                .status("success")
                .message("Books by publisher retrieved successfully")
                .data(response)
                .build();
    }

    @Override
    public BaseResponse<Page<BookResponseDTO>> findByPublisherName(String publisherName, Pageable pageable) {
        if (publisherName == null || publisherName.trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher name cannot be empty");
        }
        Page<Book> books = bookRepository.findByPublisherName(publisherName, pageable);
        Page<BookResponseDTO> response = books.map(bookMapper::toResponseDTO);
        return BaseResponse.<Page<BookResponseDTO>>builder()
                .status("success")
                .message("Books by publisher name retrieved successfully")
                .data(response)
                .build();
    }
}
