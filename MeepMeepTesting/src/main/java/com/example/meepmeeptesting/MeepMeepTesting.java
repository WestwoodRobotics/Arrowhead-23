package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        // declare a meepmeep instance
        // with a field size of 800 pixels
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-36, -58, Math.toRadians(90)))

                                // stack pixel on backboard and park (testing)
                                // temporal markers are used to run multiple thigns at once
                                // use addTemporalMarkers AND addTemporal Markers offset.

                                /*
                                .forward(20)
                                // .waitSeconds(3
                                .addTemporalMarker(2, () -> {})
                                .turn(Math.toRadians(-90))
                                .forward(35)
                                .waitSeconds(3)
                                .forward(-25)
                                .splineTo(new Vector2d(60, -59.25), 0)
                                //.splineToLinearHeading(new Pose2d(60, -59.25, Math.toRadians(-90)), 0)
                                */

                                // TODO-----------------------ParkOnly, Red, Close------------------------
                                // startPose = (11.5, -60, -90 degrees) => 1.78 seconds

                                // .strafeRight(47.5)

                                // TODO-----------------------ParkOnly, Red, Far------------------------
                                // startPose = (-36, -58, 90 degrees), => 4.37 seconds

                                .forward(48)
                                .strafeRight(95)

                                // TODO-----------------------ParkOnly, Blue, Close------------------------
                                // startPose = (11.5, 60, -90 degrees) => 1.78 seconds

                                //.strafeLeft(47.5)

                                // TODO-----------------------ParkOnly, Blue, Far------------------------
                                // (-36, 58, -90 degrees) => 4.37 seconds
                                //.forward(48)
                                //.strafeLeft(95)

                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}