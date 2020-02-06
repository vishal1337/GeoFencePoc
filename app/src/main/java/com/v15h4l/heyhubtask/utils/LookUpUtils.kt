package com.v15h4l.heyhubtask.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil

object LookUpUtils {

    /**
     * @param point point to check
     * @param vertices polygon points
     * @param accuracy allowed distance
     *
     * checking if point is in between vertices by counting intersect point
     * if intersect point is even means point is outside
     * so checking distance of that point is less or equal to allowed distance
     */

    fun isLocationWithInArea(
        point: LatLng,
        vertices: MutableList<LatLng>,
        accuracy: Int
    ): Boolean {
        var intersectCount = 0
        var isInside = false
        for (j in 0 until vertices.size - 1) {
            if (rayCastIntersect(point, vertices[j], vertices[j + 1])) {
                intersectCount++
            }
        }

        if (intersectCount % 2 == 1) {
            isInside = true
        } else {
            for (j in 0 until vertices.size - 1) {
                val nearestPoint = findNearestPoint(point, vertices)
                val distance = SphericalUtil.computeDistanceBetween(point, nearestPoint)
                isInside = distance <= accuracy
            }
        }
        return isInside
    }

    /**
     * @param point point to check
     * @param vertA polygon point 1
     * @param vertB polygon point 2
     * In this function checking point is intersect that line which is draw by two point
     * or not
     */
    private fun rayCastIntersect(point: LatLng, vertA: LatLng, vertB: LatLng): Boolean {

        val aY = vertA.latitude
        val bY = vertB.latitude
        val aX = vertA.longitude
        val bX = vertB.longitude

        val pY = point.latitude
        val pX = point.longitude

        if (aY > pY && bY > pY ||
            aY < pY && bY < pY ||
            aX < pX && bX < pX
        ) {
            return false // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        val m = (aY - bY) / (aX - bX) // Rise over run
        val bee = -aX * m + aY // y = mx + b
        val x1 = (pY - bee) / m // algebra is neat!

        return x1 > pX
    }

    /**
     * finding the nearest point from polygon
     */
    private fun findNearestPoint(point: LatLng?, target: List<LatLng>?): LatLng? {
        var distance = -1.0
        var minimumDistancePoint = point

        if (point == null || target == null) {
            return minimumDistancePoint
        }

        for (i in target.indices) {
            val point1 = target[i]

            var segmentPoint = i + 1
            if (segmentPoint >= target.size) {
                segmentPoint = 0
            }

            val currentDistance = PolyUtil.distanceToLine(point1, point1, target[segmentPoint])
            if (distance == -1.0 || currentDistance < distance) {
                distance = currentDistance
                minimumDistancePoint = findNearestPoint(point1, point1, target[segmentPoint])
            }
        }

        return minimumDistancePoint
    }

    /**
     * finding the nearest point from polygon
     */
    private fun findNearestPoint(p: LatLng, start: LatLng, end: LatLng): LatLng {
        if (start == end) {
            return start
        }

        val s0lat = Math.toRadians(p.latitude)
        val s0lng = Math.toRadians(p.longitude)
        val s1lat = Math.toRadians(start.latitude)
        val s1lng = Math.toRadians(start.longitude)
        val s2lat = Math.toRadians(end.latitude)
        val s2lng = Math.toRadians(end.longitude)

        val s2s1lat = s2lat - s1lat
        val s2s1lng = s2lng - s1lng
        val u =
            ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng) / (s2s1lat * s2s1lat + s2s1lng * s2s1lng)
        if (u <= 0) {
            return start
        }
        return if (u >= 1) {
            end
        } else LatLng(
            start.latitude + u * (end.latitude - start.latitude),
            start.longitude + u * (end.longitude - start.longitude)
        )
    }
}