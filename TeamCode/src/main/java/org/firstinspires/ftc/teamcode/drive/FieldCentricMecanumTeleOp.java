package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp (name = "field-centric")
public class FieldCentricMecanumTeleOp extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, leftBall, rightBall;
    Servo gripper, leftServo, rightServo;
    private ElapsedTime runtime = new ElapsedTime();
    double multiplier;
    int targetPosition = 1;
    int currentPosition = 0;
    boolean slowMode = false;
    BHI260IMU imu;
    Orientation angles = new Orientation();

    double initYaw;
    double adjustedYaw;


    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        /*DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");*/

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        leftBall = hardwareMap.get(DcMotorEx.class, "leftBall");
        rightBall = hardwareMap.get(DcMotorEx.class, "rightBall");

        gripper = hardwareMap.get(Servo.class, "Claw");
        leftServo = hardwareMap.get(Servo.class, "leftClaw");
        rightServo = hardwareMap.get(Servo.class, "rightClaw");


        leftBall.setDirection(DcMotorEx.Direction.FORWARD);
        rightBall.setDirection(DcMotorEx.Direction.FORWARD);

        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontRight.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);


        leftBall.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBall.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        leftBall.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftBall.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        rightBall.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightBall.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);


        gripper.setDirection(Servo.Direction.FORWARD);
        leftServo.setDirection(Servo.Direction.FORWARD);
        rightServo.setDirection(Servo.Direction.FORWARD);

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        /*frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);*/

        // Retrieve the IMU from the hardware map
        //IMU imu = hardwareMap.get(IMU.class, "imu");
        imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        ));

        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        parameters.angleUnit = BHI260IMU.AngleUnit.DEGREES;

        waitForStart();

        if (isStopRequested()) return;


        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;


            if (slowMode) {
                multiplier = 0.5;
            }
            else {
                multiplier = 1;
            }

            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.options) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = ((rotY + rotX + rx) * multiplier) / denominator;
            double backLeftPower = ((rotY - rotX + rx) * multiplier)/ denominator;
            double frontRightPower = ((rotY - rotX - rx) * multiplier)/ denominator;
            double backRightPower = ((rotY + rotX - rx) * multiplier)/ denominator;

            currentPosition = leftBall.getCurrentPosition();

            if (gamepad2.right_trigger > 0 ) {
                targetPosition -= 5;
            }
            else if (gamepad2.left_trigger > 0) {
                targetPosition += 5;
            }

            leftBall.setTargetPosition(targetPosition);
            leftBall.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

            rightBall.setTargetPosition(targetPosition);
            rightBall.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

            if (targetPosition > currentPosition) {
                leftBall.setPower(0.5);
                rightBall.setPower(0.5);
            }
            else if (targetPosition < currentPosition) {
                leftBall.setPower(-0.5);
                rightBall.setPower(-0.5);
            }
            else if (targetPosition == currentPosition) {
                leftBall.setPower(0);
                rightBall.setPower(0);
            }

            if (gamepad2.right_bumper) {
                gripper.setPosition(1);
            }
            else if (gamepad2.left_bumper) {
                gripper.setPosition(0);
            }

            if (gamepad2.dpad_down) {
                rightServo.setPosition(0);
            }
            else if (gamepad2.dpad_up) {
                rightServo.setPosition(0.5);
            }

            if (gamepad1.dpad_up) {
                slowMode = true;
            }
            else if (gamepad1.dpad_down){
                slowMode = false;
            }

            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            // for console stuff
            telemetry.addData("Status", "Run Time: ", runtime.toString());
            telemetry.addData("TargetPosForSlides: ", targetPosition);
            telemetry.addData("CurrentPosForSlides: ", currentPosition);
            telemetry.addData("Slowmode: ", slowMode);
            telemetry.addData("left stick x: ", gamepad1.left_stick_x);
            telemetry.addData("left stick y: ", gamepad1.left_stick_y);
            telemetry.addData("FLP: ", frontLeftPower);
            telemetry.addData("FRP: ", frontRightPower);
            telemetry.addData("BLP: ", backLeftPower);
            telemetry.addData("BRP: ", backRightPower);
            telemetry.addData("gamepad2-dpadUp", gamepad2.dpad_up);
        }
    }
}