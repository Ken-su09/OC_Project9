package com.suonk.oc_project9.domain.photo

import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class AddNewPhotoUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val photoRepository: PhotoRepository = mockk()

    private val addNewPhotoUseCase = AddNewPhotoUseCase(photoRepository)


    @Test
    fun `test add new photo use case`() = testCoroutineRule.runTest {
        coJustRun { photoRepository.insertPhoto(getPhotoEntity()) }

        // WHEN
        addNewPhotoUseCase.invoke(getPhotoEntity())

        coVerify { photoRepository.insertPhoto(getPhotoEntity()) }
        confirmVerified(photoRepository)
    }

    private fun getPhotoEntity(): PhotoEntity {
        return PhotoEntity(1L, 2L, "PICTURE_URL")
    }
}