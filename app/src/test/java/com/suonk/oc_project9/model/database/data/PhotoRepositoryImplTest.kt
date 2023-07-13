package com.suonk.oc_project9.model.database.data

import app.cash.turbine.test
import com.suonk.oc_project9.model.database.dao.PhotoDao
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.repositories.PhotoRepositoryImpl
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class PhotoRepositoryImplTest {

    private companion object {
        private const val PHOTO_ENTITY_DEFAULT_ID = 0L

        private const val FIRST_PHOTO_1 = "FIRST_PHOTO_1"
        private const val FIRST_PHOTO_2 = "FIRST_PHOTO_2"
        private const val FIRST_PHOTO_3 = "FIRST_PHOTO_3"
        private const val FIRST_PHOTO_4 = "FIRST_PHOTO_4"

        // SECOND
        private const val SECOND_DEFAULT_REAL_ESTATE_ID = 2L
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val photoDao: PhotoDao = mockk()

    private val photoRepositoryImpl = PhotoRepositoryImpl(photoDao)

    @Test
    fun `insert photo`() = testCoroutineRule.runTest {
        // GIVEN
        coJustRun { photoDao.insertPhoto(getDefaultPhotoEntity()) }

        // WHEN
        photoRepositoryImpl.insertPhoto(getDefaultPhotoEntity())

        // THEN
        coVerify { photoDao.insertPhoto(getDefaultPhotoEntity()) }
        confirmVerified(photoDao)
    }

    @Test
    fun `delete photo`() = testCoroutineRule.runTest {
        // GIVEN
        coJustRun { photoDao.deletePhoto(getDefaultPhotoEntity()) }

        // WHEN
        photoRepositoryImpl.deletePhoto(getDefaultPhotoEntity())

        // THEN
        coVerify { photoDao.deletePhoto(getDefaultPhotoEntity()) }
        confirmVerified(photoDao)
    }

    @Test
    fun `get list of photos by real estate id`() = testCoroutineRule.runTest {
        // GIVEN
        every { photoDao.getListOfPhotosByRealEstateId(SECOND_DEFAULT_REAL_ESTATE_ID) } returns flowOf(getDefaultSecondPhotoEntities())

        // WHEN
        photoRepositoryImpl.getListOfPhotosByRealEstateId(SECOND_DEFAULT_REAL_ESTATE_ID).test {
            // THEN
            TestCase.assertEquals(getDefaultSecondPhotoEntities(), awaitItem())
            awaitComplete()

            verify { photoDao.getListOfPhotosByRealEstateId(SECOND_DEFAULT_REAL_ESTATE_ID) }
            confirmVerified(photoDao)
        }
    }


    //region ==================================================================== DEFAULTS ====================================================================

    private fun getDefaultPhotoEntity(): PhotoEntity {
        return PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, SECOND_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_1)
    }

    private fun getDefaultSecondPhotoEntities(): List<PhotoEntity> {
        return listOf(
            getDefaultPhotoEntity(),
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, SECOND_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_2),
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, SECOND_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_3),
            PhotoEntity(PHOTO_ENTITY_DEFAULT_ID, SECOND_DEFAULT_REAL_ESTATE_ID, FIRST_PHOTO_4)
        )
    }

    //endregion
}