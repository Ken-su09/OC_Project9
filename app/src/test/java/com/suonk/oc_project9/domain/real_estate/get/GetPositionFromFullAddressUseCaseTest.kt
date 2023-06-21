package com.suonk.oc_project9.domain.real_estate.get

import android.location.Address
import android.location.Geocoder
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.suonk.oc_project9.model.database.data.entities.places.Position
import io.mockk.*
import org.junit.Before
import org.junit.Test


class GetPositionFromFullAddressUseCaseTest {

    companion object {
        private const val FAKE_GRID_ZONE = "55 West"
        private const val FAKE_STREET_NAME = "25th Street"
        private const val FAKE_CITY = "New York"
        private const val FAKE_STATE = "NY"
        private const val FAKE_POSTAL_CODE = "10010"
        private const val FAKE_FULL_ADDRESS = "$FAKE_GRID_ZONE $FAKE_STREET_NAME $FAKE_CITY $FAKE_STATE $FAKE_POSTAL_CODE"

        private const val DEFAULT_LAT = 40.7440877
        private const val DEFAULT_LONG = -73.9913408
        private val DEFAULT_POSITION = Position(lat = DEFAULT_LAT, long = DEFAULT_LONG)

//        private val FAKE_ADDRESS: Address = Address(Locale.ENGLISH)

//            Address[
//                    addressLines=[0:"55 W 25th St, New York, NY 10010, USA"],
//        feature=55,
//        admin=New York,
//        sub-admin=New York County,
//        locality=New York,
//        thoroughfare=West 25th Street,
//        postalCode=10010,
//        countryCode=US,
//        countryName=United States,
//        hasLatitude=true,
//        latitude=40.7440877,
//        hasLongitude=true,
//        longitude=-73.9913408,
//        phone=null,
//        url=null,
//        extras=null
//        )
    }

    private val geocoder: Geocoder = mockk()
    private val address: Address = mockk()

    private val getPositionFromFullAddressUseCase = GetPositionFromFullAddressUseCase(geocoder)

    @Before
    fun setup() {
        justRun {
            address.featureName = "55"
            address.adminArea = "New York"
            address.subAdminArea = "New York County"
            address.locality = "New York"
            address.thoroughfare = "West 25th Street"
            address.postalCode = "10010"
            address.countryCode = "US"
            address.countryName = "United States"
            address.latitude = DEFAULT_LAT
            address.longitude = DEFAULT_LONG
            address.phone = null
            address.url = null
            address.extras = null
        }
        every { address.latitude } returns DEFAULT_LAT
        every { address.longitude } returns DEFAULT_LONG
        every { geocoder.getFromLocationName(FAKE_FULL_ADDRESS, 1) } returns listOf(address)
    }

    @Test
    fun `test invoke get position from full address`() {
        // WHEN
        val getPositionFromFullAddress = getPositionFromFullAddressUseCase.invoke(FAKE_FULL_ADDRESS)

        // THEN
        assertThat(getPositionFromFullAddress).isEqualTo(DEFAULT_POSITION)
        verify(exactly = 1) { geocoder.getFromLocationName(FAKE_FULL_ADDRESS, 1) }
        confirmVerified(geocoder)
    }
}