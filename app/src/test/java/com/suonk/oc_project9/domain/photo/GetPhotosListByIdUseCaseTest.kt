package com.suonk.oc_project9.domain.photo

import app.cash.turbine.test
import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class GetPhotosListByIdUseCaseTest {

    companion object {
        const val defaultRealEstateId = 1L
        const val defaultRealEstateId2 = 2L

        const val defaultPhotoId = 10L
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val photoRepository: PhotoRepository = mockk()

    private val getPhotosListByIdUseCase = GetPhotosListByIdUseCase(photoRepository)


    @Test
    fun `test photos list use case`() = testCoroutineRule.runTest {
        every { photoRepository.getListOfPhotosByRealEstateId(defaultRealEstateId) } returns flowOf(getPhotoEntityList())

        // WHEN
        getPhotosListByIdUseCase.invoke(1L).test {
            // THEN
            assertEquals(getPhotoEntityList(), awaitItem())
            awaitComplete()

            coVerify { photoRepository.getListOfPhotosByRealEstateId(defaultRealEstateId) }
            confirmVerified(photoRepository)
        }
    }

    private fun getPhotoEntityList(): List<PhotoEntity> {
        return listOf(
            getPhotoEntity(),
            getPhotoEntity2(),
            getPhotoEntity3(),
            getPhotoEntity4(),
            getPhotoEntity(),
        )
    }

    private fun getPhotoEntity(): PhotoEntity {
        return PhotoEntity(defaultPhotoId, defaultRealEstateId, "PICTURE_URL")
    }

    private fun getPhotoEntity2(): PhotoEntity {
        return PhotoEntity(defaultPhotoId, defaultRealEstateId2, "PICTURE_URL")
    }

    private fun getPhotoEntity3(): PhotoEntity {
        return PhotoEntity(defaultPhotoId, defaultRealEstateId, "PICTURE_URL")
    }

    private fun getPhotoEntity4(): PhotoEntity {
        return PhotoEntity(defaultPhotoId, defaultRealEstateId, "PICTURE_URL")
    }
}