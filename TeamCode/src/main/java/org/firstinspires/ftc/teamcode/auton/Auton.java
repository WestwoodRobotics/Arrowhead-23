package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Auton-Arrowhead")
public class Auton extends LinearOpMode {
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // -----------------INIT-------------------------
        // Init: Slide1, Slide2, Servo1, Servo2, Claw's Servo
        DcMotorEx slideOne = hardwareMap.get(DcMotorEx.class, "s1");
        DcMotorEx slideTwo = hardwareMap.get(DcMotorEx.class, "s2");

        slideOne.setDirection(DcMotorEx.Direction.FORWARD);
        slideTwo.setDirection(DcMotorEx.Direction.FORWARD);

        slideOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideTwo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slideOne.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        slideOne.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        slideTwo.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        slideTwo.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        Servo clawForward1 = hardwareMap.get(Servo.class, "c1");
        Servo clawForward2 = hardwareMap.get(Servo.class, "c2");
        Servo claw = hardwareMap.get(Servo.class, "c");
       // ------------------INIT DONE--------------------

        Pose2d startPose = new Pose2d(-36, 58, Math.toRadians(-90));
        drive.setPoseEstimate(startPose);

        // TODO------------------------------------------------------------
        TrajectorySequence crossMidParkBlue = drive.trajectorySequenceBuilder(startPose)
                .forward(48)
                .strafeLeft(95)
                .build();


        if(isStopRequested()) return;


        drive.followTrajectorySequence(crossMidParkBlue);
    }
}
