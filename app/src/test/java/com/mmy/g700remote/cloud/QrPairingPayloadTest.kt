package com.mmy.g700remote.cloud

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QrPairingPayloadTest {

    @Test
    fun parsesHeadunitQrPayload() {
        val raw = """{"v":1,"relay":"wss://car.wowbooking.one","api":"https://car-api.wowbooking.one",
            "car":"car-abc123","pair":"tok-xyz","code":"4821"}"""
        val payload = QrPairingPayload.parse(raw)!!
        assertEquals(1, payload.schemaVersion)
        assertEquals("wss://car.wowbooking.one", payload.relayBase)
        assertEquals("https://car-api.wowbooking.one", payload.apiBase)
        assertEquals("car-abc123", payload.carId)
        assertEquals("tok-xyz", payload.pairToken)
        assertEquals("4821", payload.pairingCode)
    }

    @Test
    fun toBoundCarFillsDefaultsWhenBasesMissing() {
        val payload = QrPairingPayload(
            schemaVersion = 1,
            relayBase = "",
            apiBase = "",
            carId = "car-1",
            pairToken = "t",
            pairingCode = "1234",
        )
        val car = payload.toBoundCar(name = "My G700")
        assertEquals(CloudConfig.DEFAULT_API_BASE, car.apiBase)
        assertEquals(CloudConfig.DEFAULT_RELAY_BASE, car.relayBase)
        assertEquals("My G700", car.name)
    }

    @Test
    fun rejectsNonJsonAndForeignQr() {
        assertNull(QrPairingPayload.parse("https://example.com"))
        assertNull(QrPairingPayload.parse("just some text"))
        assertNull(QrPairingPayload.parse("""{"hello":"world"}"""))
    }

    @Test
    fun rejectsPayloadWithoutCodeOrToken() {
        assertNull(QrPairingPayload.parse("""{"v":1,"car":"car-1"}"""))
    }
}
