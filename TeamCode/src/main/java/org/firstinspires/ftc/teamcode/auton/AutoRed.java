package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "AutonTestScript")
public class AutoRed extends LinearOpMode {
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(11.5, -60, Math.toRadians(90));
        drive.setPoseEstimate(startPose);

/*        Trajectory myTrajectory = drive.trajectoryBuilder(new Pose2d())
                .strafeRight(47.5)
                .build();

        waitForStart();*/

        if(isStopRequested()) return;

        // drive.followTrajectory(myTrajectory);

        TrajectorySequence spit_yo_shit = drive.trajectorySequenceBuilder(startPose)
                .build()
                ;

        drive.followTrajectorySequence(spit_yo_shit);
    }
}
