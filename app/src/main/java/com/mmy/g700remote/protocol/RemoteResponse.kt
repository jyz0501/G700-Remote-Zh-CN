package com.mmy.g700remote.protocol

import org.json.JSONArray
import org.json.JSONObject

sealed class RemoteResponse {
    abstract val type: String
    abstract val raw: String

    data class HelloResult(
        val success: Boolean,
        val protocolVersion: Int?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "helloResult"
    }

    data class LockState(
        val state: Int?,
        val cabinTemp: Double?,
        val outdoorTemp: Double?,
        val coolantTemp: Double?,
        val batterySoc: Int?,
        val fuelPercent: Int?,
        val acOn: Boolean?,
        val chargingState: Int?,
        val chargeRemainTime: Int?,
        val packVoltage: Double?,
        val packCurrent: Double?,
        val packPower: Double?,
        val chargeMode: String?,
        val parkingChargeTargetSoc: Int?,
        val parkingChargeEtaMin: Int?,
        val dischargeEtaMin: Int?,
        val safetySocFloor: Int?,
        val raceChargeActive: Boolean?,
        val raceChargeTarget: Int?,
        val raceChargeEtaMin: Int?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "lockState"
    }

    data class ClimateState(
        val acOn: Boolean?,
        val tempLeft: Double?,
        val tempRight: Double?,
        val fanSpeed: Int?,
        val circulation: Int?,
        val fastCool: Boolean?,
        val fastHeat: Boolean?,
        val autoDefrost: Boolean?,
        val rearDefrost: Boolean?,
        val cabinTemp: Double?,
        val outdoorTemp: Double?,
        val coolantTemp: Double?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "climateState"
    }

    data class ParkingChargeState(
        val target: Int?,
        val switchState: Int?,
        val mode: Int?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "parkingChargeState"
    }

    data class Location(
        val lat: Double?,
        val lon: Double?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "location"
    }

    data class NavigateResult(
        val status: String?,
        val app: String?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "navigate"
    }

    data class Error(
        val error: String?,
        val message: String?,
        override val raw: String,
        val retryAfterSec: Int? = null,
    ) : RemoteResponse() {
        override val type: String = "error"
    }

    data class AudioState(
        val eqMode: Int?,
        val balance: Int?,
        val balanceMin: Int?,
        val balanceMax: Int?,
        val fade: Int?,
        val fadeMin: Int?,
        val fadeMax: Int?,
        val surround: Boolean?,
        val loudness: Boolean?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "audio"
    }

    data class CabinCoolingStateResponse(
        val enabled: Boolean?,
        val autonomous: Boolean?,
        val state: String?,
        val reason: String?,
        val cabinTemp: Double?,
        val outdoorTemp: Double?,
        val targetTemp: Double?,
        val socFloor: Int?,
        val batterySoc: Int?,
        val plugged: Boolean?,
        val keepAliveOn: Boolean?,
        val scheduleEnabled: Boolean?,
        val scheduleTime: String?,
        val scheduleLeadMinutes: Int?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "cabinCoolingState"
    }

    data class SceneResult(
        val scene: String?,
        val ok: Boolean?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "scene"
    }

    data class CameraList(
        val cameras: List<String>,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "cameraList"
    }

    data class SnapshotPending(
        val id: String?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "snapshotPending"
    }

    data class Snapshot(
        val ok: Boolean,
        val width: Int?,
        val height: Int?,
        val dataBase64: String?,
        val error: String?,
        val id: String?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "snapshot"
    }

    data class LiveViewState(
        val state: String?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "liveView"
    }

    data class LiveFrame(
        val seq: Int?,
        val width: Int?,
        val height: Int?,
        val dataBase64: String?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "liveFrame"
    }

    data class SentinelState(
        val state: String?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "sentinel"
    }

    data class SentinelAlert(
        val event: Int?,
        val eventName: String?,
        val time: String?,
        val thumbBase64: String?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "sentinelAlert"
    }

    data class ObdState(
        val connected: Boolean?,
        val ageMs: Long?,
        val rpm: Double?,
        val speed: Double?,
        val coolant: Double?,
        val fuel: Double?,
        val intakeTemp: Double?,
        val ambientTemp: Double?,
        val throttle: Double?,
        val load: Double?,
        val moduleVoltage: Double?,
        val batteryVoltage: Double?,
        val vin: String?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "obd"
    }

    data class TelemetryState(
        val latAccel: Double?,
        val lonAccel: Double?,
        val yaw: Double?,
        val steering: Double?,
        val heightLF: Double?,
        val heightRF: Double?,
        val heightLR: Double?,
        val heightRR: Double?,
        val speed: Double?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "telemetry"
    }

    data class CabinState(
        val child: Int?,
        val pet: Int?,
        val occupant: Int?,
        val seatbelt: Int?,
        val drowsiness: Int?,
        val distraction: Int?,
        override val raw: String,
    ) : RemoteResponse() {
        override val type: String = "cabin"
    }

    data class Unknown(
        override val type: String,
        override val raw: String,
    ) : RemoteResponse()

    companion object {
        fun fromJson(raw: String): RemoteResponse {
            val json = JSONObject(raw)
            return when (val type = json.optString("type", "")) {
                "helloResult" -> HelloResult(
                    success = json.optBoolean("success", false),
                    protocolVersion = json.optIntOrNull("protocolVersion"),
                    raw = raw,
                )

                "lockState" -> LockState(
                    state = json.optIntOrNull("state"),
                    cabinTemp = json.optDoubleOrNull("cabinTemp"),
                    outdoorTemp = json.optDoubleOrNull("outdoorTemp"),
                    coolantTemp = json.optDoubleOrNull("coolantTemp"),
                    batterySoc = json.optIntOrNull("batterySOC"),
                    fuelPercent = json.optIntOrNull("fuelPercent"),
                    acOn = json.optBooleanOrNull("acOn"),
                    chargingState = json.optIntOrNull("chargingState"),
                    chargeRemainTime = json.optIntOrNull("chargeRemainTime"),
                    packVoltage = json.optDoubleOrNull("packVoltage"),
                    packCurrent = json.optDoubleOrNull("packCurrent"),
                    packPower = json.optDoubleOrNull("packPower"),
                    chargeMode = json.optStringOrNull("chargeMode"),
                    parkingChargeTargetSoc = json.optIntOrNull("parkingChargeTargetSOC"),
                    parkingChargeEtaMin = json.optIntOrNull("parkingChargeEtaMin"),
                    dischargeEtaMin = json.optIntOrNull("dischargeEtaMin"),
                    safetySocFloor = json.optIntOrNull("safetySocFloor"),
                    raceChargeActive = json.optBooleanOrNull("raceChargeActive"),
                    raceChargeTarget = json.optIntOrNull("raceChargeTarget"),
                    raceChargeEtaMin = json.optIntOrNull("raceChargeEtaMin"),
                    raw = raw,
                )

                "climateState" -> ClimateState(
                    acOn = json.optBooleanOrNull("acOn"),
                    tempLeft = json.optDoubleOrNull("tempLeft"),
                    tempRight = json.optDoubleOrNull("tempRight"),
                    fanSpeed = json.optIntOrNull("fanSpeed"),
                    circulation = json.optIntOrNull("circulation"),
                    fastCool = json.optBooleanOrNull("fastCool"),
                    fastHeat = json.optBooleanOrNull("fastHeat"),
                    autoDefrost = json.optBooleanOrNull("autoDefrost"),
                    rearDefrost = json.optBooleanOrNull("rearDefrost"),
                    cabinTemp = json.optDoubleOrNull("cabinTemp"),
                    outdoorTemp = json.optDoubleOrNull("outdoorTemp"),
                    coolantTemp = json.optDoubleOrNull("coolantTemp"),
                    raw = raw,
                )

                "parkingChargeState" -> ParkingChargeState(
                    target = json.optIntOrNull("target"),
                    switchState = json.optIntOrNull("switchState"),
                    mode = json.optIntOrNull("mode"),
                    raw = raw,
                )

                "location" -> Location(
                    lat = json.optDoubleOrNull("lat"),
                    lon = json.optDoubleOrNull("lon"),
                    raw = raw,
                )

                "navigate" -> NavigateResult(
                    status = json.optStringOrNull("status"),
                    app = json.optStringOrNull("app"),
                    raw = raw,
                )

                "error" -> Error(
                    error = json.optStringOrNull("error"),
                    message = json.optStringOrNull("message"),
                    retryAfterSec = json.optIntOrNull("retryAfterSec"),
                    raw = raw,
                )

                "audio" -> AudioState(
                    eqMode = json.optIntOrNull("eqMode"),
                    balance = json.optIntOrNull("balance"),
                    balanceMin = json.optIntOrNull("balanceMin"),
                    balanceMax = json.optIntOrNull("balanceMax"),
                    fade = json.optIntOrNull("fade"),
                    fadeMin = json.optIntOrNull("fadeMin"),
                    fadeMax = json.optIntOrNull("fadeMax"),
                    surround = json.optBooleanOrNull("surround"),
                    loudness = json.optBooleanOrNull("loudness"),
                    raw = raw,
                )

                "obd" -> ObdState(
                    connected = json.optBooleanOrNull("connected"),
                    ageMs = if (json.has("ageMs") && !json.isNull("ageMs")) json.optLong("ageMs") else null,
                    rpm = json.optDoubleOrNull("rpm"),
                    speed = json.optDoubleOrNull("speed"),
                    coolant = json.optDoubleOrNull("coolant"),
                    fuel = json.optDoubleOrNull("fuel"),
                    intakeTemp = json.optDoubleOrNull("intakeTemp"),
                    ambientTemp = json.optDoubleOrNull("ambientTemp"),
                    throttle = json.optDoubleOrNull("throttle"),
                    load = json.optDoubleOrNull("load"),
                    moduleVoltage = json.optDoubleOrNull("moduleVoltage"),
                    batteryVoltage = json.optDoubleOrNull("batteryVoltage"),
                    vin = json.optStringOrNull("vin"),
                    raw = raw,
                )

                "telemetry" -> TelemetryState(
                    latAccel = json.optDoubleOrNull("latAccel"),
                    lonAccel = json.optDoubleOrNull("lonAccel"),
                    yaw = json.optDoubleOrNull("yaw"),
                    steering = json.optDoubleOrNull("steering"),
                    heightLF = json.optDoubleOrNull("heightLF"),
                    heightRF = json.optDoubleOrNull("heightRF"),
                    heightLR = json.optDoubleOrNull("heightLR"),
                    heightRR = json.optDoubleOrNull("heightRR"),
                    speed = json.optDoubleOrNull("speed"),
                    raw = raw,
                )

                "cabin" -> CabinState(
                    child = json.optIntOrNull("child"),
                    pet = json.optIntOrNull("pet"),
                    occupant = json.optIntOrNull("occupant"),
                    seatbelt = json.optIntOrNull("seatbelt"),
                    drowsiness = json.optIntOrNull("drowsiness"),
                    distraction = json.optIntOrNull("distraction"),
                    raw = raw,
                )

                "cabinCoolingState" -> {
                    val schedule = json.optJSONObject("schedule")
                    CabinCoolingStateResponse(
                        enabled = json.optBooleanOrNull("enabled"),
                        autonomous = json.optBooleanOrNull("autonomous"),
                        state = json.optStringOrNull("state"),
                        reason = json.optStringOrNull("reason"),
                        cabinTemp = json.optDoubleOrNull("cabinTemp"),
                        outdoorTemp = json.optDoubleOrNull("outdoorTemp"),
                        targetTemp = json.optDoubleOrNull("targetTemp"),
                        socFloor = json.optIntOrNull("socFloor"),
                        batterySoc = json.optIntOrNull("batterySOC"),
                        plugged = json.optBooleanOrNull("plugged"),
                        keepAliveOn = json.optBooleanOrNull("keepAliveOn"),
                        scheduleEnabled = schedule?.optBooleanOrNull("enabled"),
                        scheduleTime = schedule?.optStringOrNull("time"),
                        scheduleLeadMinutes = schedule?.optIntOrNull("leadMinutes"),
                        raw = raw,
                    )
                }

                "scene" -> SceneResult(
                    scene = json.optStringOrNull("scene"),
                    ok = json.optBooleanOrNull("ok"),
                    raw = raw,
                )

                "cameraList" -> CameraList(
                    cameras = json.optJSONArray("cameras").toStringList(),
                    raw = raw,
                )

                "snapshotPending" -> SnapshotPending(
                    id = json.optStringOrNull("id"),
                    raw = raw,
                )

                "snapshot" -> Snapshot(
                    ok = json.optBoolean("ok", false),
                    width = json.optIntOrNull("w"),
                    height = json.optIntOrNull("h"),
                    dataBase64 = json.optStringOrNull("data"),
                    error = json.optStringOrNull("error"),
                    id = json.optStringOrNull("id"),
                    raw = raw,
                )

                "liveView" -> LiveViewState(
                    state = json.optStringOrNull("state"),
                    raw = raw,
                )

                "liveFrame" -> LiveFrame(
                    seq = json.optIntOrNull("seq"),
                    width = json.optIntOrNull("w"),
                    height = json.optIntOrNull("h"),
                    dataBase64 = json.optStringOrNull("data"),
                    raw = raw,
                )

                "sentinel" -> SentinelState(
                    state = json.optStringOrNull("state"),
                    raw = raw,
                )

                "sentinelAlert" -> SentinelAlert(
                    event = json.optIntOrNull("event"),
                    eventName = json.optStringOrNull("eventName"),
                    time = json.optStringOrNull("time"),
                    thumbBase64 = json.optStringOrNull("thumb"),
                    raw = raw,
                )

                else -> Unknown(type = type.ifBlank { "unknown" }, raw = raw)
            }
        }
    }
}

private fun JSONObject.optIntOrNull(name: String): Int? =
    if (has(name) && !isNull(name)) optInt(name) else null

private fun JSONObject.optDoubleOrNull(name: String): Double? =
    if (has(name) && !isNull(name)) optDouble(name) else null

private fun JSONObject.optBooleanOrNull(name: String): Boolean? =
    if (has(name) && !isNull(name)) optBoolean(name) else null

private fun JSONObject.optStringOrNull(name: String): String? =
    if (has(name) && !isNull(name)) optString(name) else null

private fun JSONArray?.toStringList(): List<String> {
    if (this == null) return emptyList()
    return (0 until length()).mapNotNull { index ->
        if (isNull(index)) null else optString(index, null)?.takeIf { it.isNotEmpty() }
    }
}
