package com.example.decoupled_android_search.core.use_cases.dog_search

import com.example.decoupled_android_search.core.use_cases.dog_search.infra.PaginatedDogRepository
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.net.URL
import java.util.stream.Stream

@ExtendWith(MockitoExtension::class)
class DogSearchInteractorTest {
    @Mock
    lateinit var repository: PaginatedDogRepository

    lateinit var useCase: DogSearchUseCase

    @BeforeEach
    fun setUp() {
        useCase = DogSearchInteractor(repository)
    }

    @Test
    fun shouldGetAllBreeds() {
        val expectedBreeds = listOf(
            Breed("golden"),
            Breed("akita")
        )
        given(repository.getAllBreeds())
            .willReturn(expectedBreeds)

        val resultBreeds = useCase.getBreeds()

        assertThat(resultBreeds).isEqualTo(expectedBreeds)
    }

    @Test
    fun shouldThrowSearchExceptionWhenSearchingBreeds() {
        given(repository.getAllBreeds())
            .willAnswer{throw PaginatedDogRepository.SearchException()}

        assertThrows(DogSearchInteractor.SearchException::class.java) {
            useCase.getBreeds()
        }
    }

    @Test
    fun shouldGetSubBreeds() {
        val breed = Breed("hound")
        val expectedSubBreeds = listOf(
            SubBreed(breed, "afghan"),
            SubBreed(breed, "basset"),
            SubBreed(breed, "blood"),
            SubBreed(breed, "english"),
            SubBreed(breed, "ibizan"),
            SubBreed(breed, "plott"),
            SubBreed(breed, "walker")
        )
        given(repository.getSubBreeds(breed))
            .willReturn(expectedSubBreeds)

        val resultSubBreeds = useCase.getSubBreeds(breed)

        assertThat(resultSubBreeds).isEqualTo(expectedSubBreeds)
    }

    @Test
    fun shouldThrowSearchExceptionWhenSearchingSubBreeds() {
        given(repository.getSubBreeds(any()))
            .willAnswer{throw PaginatedDogRepository.SearchException()}

        assertThrows(DogSearchInteractor.SearchException::class.java) {
            useCase.getSubBreeds(Breed("hound"))
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ImagesOfBreedByPageArgumentsProvider::class)
    fun shouldGetImagesOfBreedByPage(breed: Breed, page: Int, expectedUrls: List<URL>) {
        given(repository.getBreedImagesByPage(breed, page))
            .willReturn(expectedUrls)

        val resultUrls = useCase.getImages(breed, page)

        assertThat(resultUrls).isEqualTo(expectedUrls)
    }

    @Test
    fun shouldThrowSearchExceptionWhenGettingImagesOfBreedByPage() {
        given(repository.getBreedImagesByPage(any(), any<Int>()))
            .willAnswer {throw PaginatedDogRepository.SearchException()}

        assertThrows(DogSearchInteractor.SearchException::class.java) {
            useCase.getImages(Breed("akita"), 1)
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ImagesOfSubBreedByPageArgumentsProvider::class)
    fun shouldGetImagesOfSubBreedByPage(subBreed: SubBreed, page: Int, expectedUrls: List<URL>) {
        given(repository.getSubBreedImagesByPage(subBreed, page))
            .willReturn(expectedUrls)

        val resultUrls = useCase.getImages(subBreed, page)

        assertThat(resultUrls).isEqualTo(expectedUrls)
    }

    @Test
    fun shouldThrowSearchExceptionWhenGettingImagesOfSubBreedByPage() {
        given(repository.getSubBreedImagesByPage(any(), any<Int>()))
            .willAnswer {throw PaginatedDogRepository.SearchException()}

        assertThrows(DogSearchInteractor.SearchException::class.java) {
            useCase.getImages(
                SubBreed(Breed("bulldog"), "english"),
                1
            )
        }
    }
}

class ImagesOfBreedByPageArgumentsProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                Breed("akita"), 0, listOf(URL("https://www.google.com"))
            ),
            Arguments.of(
                Breed("doberman"), 1, emptyList<URL>()
            ),
            Arguments.of(
                Breed("dalmatian"),
                0,
                listOf(
                    URL("https://www.google.com"),
                    URL("https://www.en.wikipedia.org"),
                )
            ),
        )
    }
}

class ImagesOfSubBreedByPageArgumentsProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        val bulldog = Breed("bulldog")
        return Stream.of(
            Arguments.of(
                SubBreed(bulldog, "boston"),
                0,
                listOf(URL("https://www.google.com"))
            ),
            Arguments.of(
                SubBreed(bulldog, "boston"),
                1,
                emptyList<URL>()
            ),
            Arguments.of(
                SubBreed(bulldog, "english"),
                0,
                listOf(
                    URL("https://www.google.com"),
                    URL("https://www.en.wikipedia.org"),
                )
            ),
        )
    }
}
