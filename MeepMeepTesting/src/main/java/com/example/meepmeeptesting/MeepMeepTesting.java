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
                        drive.trajectorySequenceBuilder(new Pose2d(11.25, -60, Math.toRadians(90)))

                                // stack pixel on backboard and park
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

                                // park - red - right side (11.5, -60)
                                 .strafeRight(47.5)

                                // park - red - left side (-36, -58)
                                //.forward(35)
                                //.strafeRight(100)

                                //.strafeLeft(47.5) // blue side park)

                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}