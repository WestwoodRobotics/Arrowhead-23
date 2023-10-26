package org.firstinspires.ftc.teamcode.drive.opmode;
import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
// field centric stuff
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import java.util.*;

// TODO: check field centric mode
// TODO: literally test the entire TeleOp drive LOL
// slide1 is left ball, slide2 is right ball
// i've left the non-field centric code in comments in the loop function, just in case field centric turns out to be awful
@TeleOp(name = "TeleOp")
public class TeleOpDriveOpMode extends OpMode{
    DcMotorEx frontLeft, frontRight, backLeft, backRight, leftBall, rightBall;

    // IMU var for field centric mode
    BHI260IMU babymode;

    private ElapsedTime runtime  =  new ElapsedTime();

    public void init() {

        babymode = hardwareMap.get(BHI260IMU.class, "IMU");

        leftBall = hardwareMap.get(DcMotorEx.class, "leftBall");
        rightBall = hardwareMap.get(DcMotorEx.class, "rightBall");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        leftBall.setDirection(DcMotorSimple.Direction.FORWARD);

        // All set to forward so we can test if any motors are backwards
        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontRight.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);

        // important slide shit
        leftBall.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // Encoders
        leftBall.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftBall.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // drive train using encoders? most likely for roadrunner but this may be removed
        rightBall.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightBall.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        backLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        backRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // IMU parameters that we probably have to change once we get to testing the manual mode
        BHI260IMU.Parameters imuParams = new BHI260IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

        babymode.initialize(imuParams);
    }

    // these variables are currently controlling both slides because im assuming they're working in uniform and not independant of each other's actions
    int targetPosition = 1;
    int currentPosition = 0;
    boolean slowMode = false;

    public void loop() {
        // this is drive, strafe, and turn, from the top down
        double fourDriveMechanumlinearY = -gamepad1.left_stick_y; // Y is reversed
        double fourDriveMechanumlinearX = gamepad1.left_stick_x;
        double fourDriveMechanumRotationalX = gamepad1.right_stick_x;
        
        // slowmode uhhhuhhhuhhhh scale thing idfk LOL
        double multiplier;

        if (slowMode) {
            multiplier = 0.5;
        }
        else {
            multiplier = 1;
        }

        // make it so it's not easy to accidently hit the button
        // think of this like the xbox start button
        // can be changed if needed
        if (gamepad1.options) {
            babymode.resetYaw();
        }

        double botHeading = babymode.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        
        // rotate the movement direction counter to the bot's rotation
        double rotX = fourDriveMechanumlinearX * Math.cos(-botHeading) - fourDriveMechanumlinearY * Math.sin(-botHeading);
        double rotY = fourDriveMechanumlinearX * Math.sin(-botHeading) + fourDriveMechanumlinearY * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // counteract imperfect strafing

        /*
        double fourDriveMechanumFrontLeftPower = (fourDriveMechanumlinearY + fourDriveMechanumlinearX + fourDriveMechanumRotationalX) * multiplier;
        double fourDriveMechanumBackLeftPower = (fourDriveMechanumlinearY - fourDriveMechanumlinearX + fourDriveMechanumRotationalX) * multiplier;
        double fourDriveMechanumFrontRightPower = (fourDriveMechanumlinearY - fourDriveMechanumlinearX - fourDriveMechanumRotationalX) * multiplier;
        double fourDriveMechanumBackRightPower = (fourDriveMechanumlinearY + fourDriveMechanumlinearX - fourDriveMechanumRotationalX) * multiplier;
        fix ???
        */

        // denom is largest motor power (abs volue) or 1
        // ensures all powers maintain the same ratio
        // ONLY IF at least one is out of range [-1, 1]
        double denom = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(fourDriveMechanumRotationalX), 1);

        double fieldCentricFLP = ((rotY + rotX + fourDriveMechanumRotationalX) * multiplier) / denom;
        double fieldCentricBLP = ((rotY - rotX + fourDriveMechanumRotationalX) * multiplier) / denom;
        double fieldCentricFRP = ((rotY - rotX - fourDriveMechanumRotationalX) * multiplier) / denom;
        double fieldCentricBRP = ((rotY + rotX - fourDriveMechanumRotationalX) * multiplier) / denom;

         currentPosition = leftBall.getCurrentPosition();

        if (gamepad1.left_trigger > 0 ) {
            targetPosition -= 5;
        }
        else if (gamepad1.right_trigger > 0) {
            targetPosition += 5;
        }

        /*
        A bunch of stuff related to the slides, I have no idea the specifics anymore
         */
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

        /*
        Enabling of precise control mode, if it even works
         */
        if (gamepad1.dpad_up) {
            slowMode = true;
        }
        // idk what this else is checking for, worst case scenario, assign a specific button to turn precision mode off
        else {
            slowMode = false;
        }

        /*
        frontLeft.setPower(fourDriveMechanumFrontLeftPower);
        backLeft.setPower(fourDriveMechanumBackLeftPower);
        frontRight.setPower(fourDriveMechanumFrontRightPower);
        backRight.setPower(fourDriveMechanumBackRightPower);
        */

        frontLeft.setPower(fieldCentricFLP);
        backLeft.setPower(fieldCentricBLP);
        frontRight.setPower(fieldCentricFRP);
        backRight.setPower(fieldCentricBRP);


        // for console stuff
        telemetry.addData("Status", "Run Time: ", runtime.toString());
        telemetry.addData("TargetPos: ", targetPosition);
        telemetry.addData("CurrentPos: ", currentPosition);
        telemetry.addData("Slowmode: ", slowMode);
    }
}
