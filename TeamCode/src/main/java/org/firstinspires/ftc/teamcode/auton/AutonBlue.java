package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "BlueClose")
public class AutonBlue extends LinearOpMode {
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(11.5, 60, Math.toRadians(-90));

        drive.setPoseEstimate(startPose);

        TrajectorySequence simpleParkBlue = drive.trajectorySequenceBuilder(startPose)
                .strafeLeft(47.5)
                .build();

        if(isStopRequested()) return;

        drive.followTrajectorySequence(simpleParkBlue);
    }
}
