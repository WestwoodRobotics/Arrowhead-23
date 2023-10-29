package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "RedFar")
public class AutonRedFar extends LinearOpMode {
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(-36, -58, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        TrajectorySequence crossMidParkRed = drive.trajectorySequenceBuilder(startPose)
                .forward(48)
                .strafeRight(95)
                .build();

        if(isStopRequested()) return;

        drive.followTrajectorySequence(crossMidParkRed);
    }
}
