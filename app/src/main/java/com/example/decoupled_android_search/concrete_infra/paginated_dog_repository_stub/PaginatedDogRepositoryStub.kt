package com.example.decoupled_android_search.concrete_infra.paginated_dog_repository_stub

import com.example.decoupled_android_search.core.entities.Breed
import com.example.decoupled_android_search.core.entities.SubBreed
import com.example.decoupled_android_search.core.use_cases.dog_search.infra.PaginatedDogRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.net.URL

class PaginatedDogRepositoryStub: PaginatedDogRepository {
    override fun getAllBreeds(): List<Breed> {
        runBlocking {
            delay(2000L)
        }
        return breeds
    }

    override fun getSubBreeds(breed: Breed): List<SubBreed> {
        runBlocking {
            delay(2000L)
        }
        return subBreeds[breed] ?: emptyList()
    }

    override fun getBreedImagesByPage(breed: Breed, page: Int): List<URL> {
        val startIndex = 10 * page
        val endIndex   = startIndex + 10

        runBlocking {
            delay(2000L)
        }
        return if (endIndex > images.size)
            emptyList()
        else
            images.subList(startIndex, endIndex)
    }

    override fun getSubBreedImagesByPage(subBreed: SubBreed, page: Int): List<URL> {
        val startIndex = 10 * page
        val endIndex   = startIndex + 10
        return images.subList(startIndex, endIndex)
    }
}

private val breeds = listOf(
    Breed("akita"),
    Breed("affenpinscher"),
    Breed("african"),
    Breed("airedale"),
    Breed("akita"),
    Breed("appenzeller"),
    Breed("australian"),
    Breed("basenji"),
    Breed("beagle"),
    Breed("bluetick"),
    Breed("borzoi"),
    Breed("bouvier"),
    Breed("boxer"),
    Breed("brabancon"),
    Breed("briard"),
    Breed("buhund"),
    Breed("bulldog"),
)

private val subBreeds = mapOf(
    Breed("australian") to listOf(
        SubBreed(Breed("australian"), "shepherd")
    ),
    Breed("buhund") to listOf(
        SubBreed(Breed("buhund"), "norwegian"),
    ),
    Breed("bulldog") to listOf(
        SubBreed(Breed("bulldog"), "boston"),
        SubBreed(Breed("bulldog"), "english"),
        SubBreed(Breed("bulldog"), "french"),
    ),
)

private val images = listOf(
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4426.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4434.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4450.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4464.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4467.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4497.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4501.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4511.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4517.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4521.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4583.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4586.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4598.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4635.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4678.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_472.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4759.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4768.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_482.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_4837.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_8682.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_875.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_8764.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_890.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_899.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_907.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_908.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_913.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_9197.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_9220.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_9229.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_93.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_9523.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_980.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_986.jpg"),
    URL("https://images.dog.ceo/breeds/hound-afghan/n02088094_988.jpg"),
    URL("https://images.dog.ceo/breeds/hound-basset/n02088238_10005.jpg"),
    URL("https://images.dog.ceo/breeds/hound-basset/n02088238_10013.jpg"),
    URL("https://images.dog.ceo/breeds/hound-basset/n02088238_10028.jpg"),
    URL("https://images.dog.ceo/breeds/hound-basset/n02088238_10049.jpg"),
    URL("https://images.dog.ceo/breeds/hound-basset/n02088238_10051.jpg"),
    URL("https://images.dog.ceo/breeds/hound-basset/n02088238_10054.jpg"),
    URL("https://images.dog.ceo/breeds/hound-basset/n02088238_10063.jpg"),
    URL("https://images.dog.ceo/breeds/hound-basset/n02088238_10072.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_8926.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_8950.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_8969.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_8982.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9023.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9029.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9046.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9058.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9069.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9096.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9110.jpg"),
    URL("https://images.dog.ceo/breeds/houd-blood/n02088466_9116.jpgj"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9167.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9191.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9214.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9237.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9242.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9245.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9287.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9334.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9356.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9359.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9360.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9383.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9533.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9576.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9579.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9616.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9691.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9695.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9697.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9792.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9877.jpg"),
    URL("https://images.dog.ceo/breeds/hound-blood/n02088466_9983.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1000.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1030.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1066.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1076.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1078.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1106.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1132.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1232.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1249.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1255.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1260.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1277.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1298.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1303.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1312.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1324.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1345.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1352.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1356.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1357.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1375.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1381.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_140.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1458.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1490.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1492.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1516.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1577.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1623.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1690.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1701.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1733.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1748.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1763.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1799.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1803.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1841.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_186.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1877.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1888.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1890.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1907.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_1957.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2017.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2045.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2054.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2064.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2068.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2073.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_208.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2093.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2110.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2133.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2150.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2181.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2211.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2249.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2300.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2308.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2322.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2331.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2345.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2397.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2404.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2415.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_243.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2457.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2476.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2484.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2497.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2500.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2509.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_251.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_255.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2551.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2599.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2603.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2608.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_263.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2633.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2681.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_270.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2716.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2756.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2781.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2791.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2810.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_289.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2905.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_2943.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3037.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3040.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3055.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3074.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3089.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3113.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3119.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3120.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3136.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3147.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3156.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3159.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3160.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_319.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_32.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3243.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3265.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3299.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3307.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3323.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3401.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3420.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3426.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3433.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3454.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3480.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3604.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3647.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3651.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3671.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3688.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3726.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3799.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_382.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3820.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3860.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3933.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_3982.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_4010.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_4055.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_4084.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_417.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_4185.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_4307.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_4359.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_437.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_48.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_504.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_525.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_529.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_533.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_556.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_569.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_612.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_770.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_809.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_811.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_843.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_846.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_888.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_957.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_97.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_973.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_981.jpg"),
    URL("https://images.dog.ceo/breeds/hound-english/n02089973_99.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_100.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1000.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1025.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1030.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1037.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1045.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1072.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1076.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_110.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1120.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1182.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_119.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1238.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1252.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1265.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1283.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1340.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1371.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1396.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_142.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1438.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_147.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1488.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1496.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_150.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1525.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1541.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_156.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1635.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_169.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1690.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1714.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1737.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_175.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_177.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1856.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1917.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1938.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_1948.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2038.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2050.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2062.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2115.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2118.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2132.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2198.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2207.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_226.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_227.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2275.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2293.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2323.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2407.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2427.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2464.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_25.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2512.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2542.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2601.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2625.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2648.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2653.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2695.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2709.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2742.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2747.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2760.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2782.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2820.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_283.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2838.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2871.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_289.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2902.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_291.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2919.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2934.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2941.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_2993.jpg"),
    URL("https://images.dog.ceo/breeds/hound-ibizan/n02091244_3005.jpg"),
)