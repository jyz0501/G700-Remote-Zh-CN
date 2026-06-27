package com.mmy.g700remote.protocol

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Serialization/parsing coverage for the DisplayMirror 3.x command surface added on
 * top of the protocol-v4 baseline: cameras, snapshot, live_view, sentinel, scene,
 * audio, audio_set, cabin_cooling, and the new climate hvac actions.
 */
class V3ProtocolTest {

    @Test
    fun camerasCommandSerializes() {
        val json = JSONObject(RemoteProtocolCodec.encodeCommand(RemoteCommand.Cameras))
        assertEquals("cameras", json.getString("cmd"))
    }

    @Test
    fun snapshotCommandIncludesCameraAndId() {
        val json = JSONObject(
            RemoteProtocolCodec.encodeCommand(RemoteCommand.Snapshot(camera = "1", requestId = "req-7")),
        )
        assertEquals("snapshot", json.getString("cmd"))
        assertEquals("1", json.getString("camera"))
        assertEquals("req-7", json.getString("id"))
    }

    @Test
    fun liveViewCommandSerializesActionAndCamera() {
        val json = JSONObject(
            RemoteProtocolCodec.encodeCommand(
                RemoteCommand.LiveView(StartStopAction.Start, camera = "0"),
            ),
        )
        assertEquals("live_view", json.getString("cmd"))
        assertEquals("start", json.getString("action"))
        assertEquals("0", json.getString("camera"))
    }

    @Test
    fun sceneCommandSerializesWireValue() {
        val json = JSONObject(RemoteProtocolCodec.encodeCommand(RemoteCommand.Scene(SceneKind.Cinema)))
        assertEquals("scene", json.getString("cmd"))
        assertEquals("cinema", json.getString("scene"))
    }

    @Test
    fun carWashSceneIsSensitive() {
        assertTrue(RemoteCommand.Scene(SceneKind.CarWash).sensitive)
        assertFalse(RemoteCommand.Scene(SceneKind.Cinema).sensitive)
    }

    @Test
    fun audioSetSerializesBooleanAndIntValues() {
        val loud = JSONObject(
            RemoteProtocolCodec.encodeCommand(RemoteCommand.AudioSet(AudioSetAction.Loudness, true)),
        )
        assertEquals("audio_set", loud.getString("cmd"))
        assertEquals("loudness", loud.getString("action"))
        assertTrue(loud.getBoolean("value"))

        val balance = JSONObject(
            RemoteProtocolCodec.encodeCommand(RemoteCommand.AudioSet(AudioSetAction.Balance, 3)),
        )
        assertEquals("balance", balance.getString("action"))
        assertEquals(3, balance.getInt("value"))
    }

    @Test
    fun cabinCoolingMergesConfigPayload() {
        val config = JSONObject().put("targetTemp", 24).put("socFloor", 30)
        val json = JSONObject(
            RemoteProtocolCodec.encodeCommand(
                RemoteCommand.CabinCooling(CabinCoolingAction.SetConfig, config),
            ),
        )
        assertEquals("cabin_cooling", json.getString("cmd"))
        assertEquals("set_config", json.getString("action"))
        assertEquals(24, json.getInt("targetTemp"))
        assertEquals(30, json.getInt("socFloor"))
    }

    @Test
    fun hvacClimateActionsSerialize() {
        val on = JSONObject(
            RemoteProtocolCodec.encodeCommand(RemoteCommand.Climate(ClimateAction.HvacOn)),
        )
        assertEquals("climate", on.getString("cmd"))
        assertEquals("hvac_on", on.getString("action"))
    }

    @Test
    fun cameraListParses() {
        val response = RemoteResponse.fromJson("""{"type":"cameraList","cameras":["0","1","2"]}""")
        assertTrue(response is RemoteResponse.CameraList)
        assertEquals(listOf("0", "1", "2"), (response as RemoteResponse.CameraList).cameras)
    }

    @Test
    fun snapshotFrameParses() {
        val response = RemoteResponse.fromJson(
            """{"type":"snapshot","ok":true,"w":1280,"h":720,"data":"AAAA","id":"req-7"}""",
        )
        assertTrue(response is RemoteResponse.Snapshot)
        response as RemoteResponse.Snapshot
        assertTrue(response.ok)
        assertEquals(1280, response.width)
        assertEquals("AAAA", response.dataBase64)
        assertEquals("req-7", response.id)
    }

    @Test
    fun liveFrameParses() {
        val response = RemoteResponse.fromJson("""{"type":"liveFrame","seq":12,"w":480,"h":270,"data":"ZZ"}""")
        assertTrue(response is RemoteResponse.LiveFrame)
        assertEquals(12, (response as RemoteResponse.LiveFrame).seq)
    }

    @Test
    fun sentinelAlertParses() {
        val response = RemoteResponse.fromJson(
            """{"type":"sentinelAlert","event":3,"eventName":"Motion near your car","thumb":"QQ"}""",
        )
        assertTrue(response is RemoteResponse.SentinelAlert)
        response as RemoteResponse.SentinelAlert
        assertEquals(3, response.event)
        assertEquals("Motion near your car", response.eventName)
        assertEquals("QQ", response.thumbBase64)
    }

    @Test
    fun audioStateParses() {
        val response = RemoteResponse.fromJson(
            """{"type":"audio","eqMode":2,"balance":0,"fade":1,"surround":true,"loudness":false}""",
        )
        assertTrue(response is RemoteResponse.AudioState)
        response as RemoteResponse.AudioState
        assertEquals(2, response.eqMode)
        assertEquals(true, response.surround)
        assertEquals(false, response.loudness)
    }

    @Test
    fun cabinCoolingStateParsesNestedSchedule() {
        val response = RemoteResponse.fromJson(
            """{"type":"cabinCoolingState","enabled":true,"state":"idle","targetTemp":24,
               "socFloor":30,"schedule":{"enabled":true,"time":"07:30","leadMinutes":15}}""",
        )
        assertTrue(response is RemoteResponse.CabinCoolingStateResponse)
        response as RemoteResponse.CabinCoolingStateResponse
        assertEquals(true, response.enabled)
        assertEquals("idle", response.state)
        assertEquals(true, response.scheduleEnabled)
        assertEquals("07:30", response.scheduleTime)
        assertEquals(15, response.scheduleLeadMinutes)
    }

    @Test
    fun lockedOutErrorCarriesRetryAfter() {
        val response = RemoteResponse.fromJson(
            """{"type":"error","error":"locked_out","message":"Too many","retryAfterSec":60}""",
        )
        assertTrue(response is RemoteResponse.Error)
        assertEquals(60, (response as RemoteResponse.Error).retryAfterSec)
    }

    @Test
    fun unmatchedTypeStaysUnknown() {
        val response = RemoteResponse.fromJson("""{"type":"telemetry","speed":80}""")
        assertTrue(response is RemoteResponse.Unknown)
        assertEquals("telemetry", response.type)
    }

    @Test
    fun snapshotPendingHasNoData() {
        val response = RemoteResponse.fromJson("""{"type":"snapshotPending","id":"req-7"}""")
        assertTrue(response is RemoteResponse.SnapshotPending)
        assertEquals("req-7", (response as RemoteResponse.SnapshotPending).id)
        assertNull(JSONObject(response.raw).optString("data", null))
    }
}
