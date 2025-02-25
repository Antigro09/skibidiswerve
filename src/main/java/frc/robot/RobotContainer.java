// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.Constants.*;
import static frc.robot.subsystems.drive.DriveConstants.*;
import static frc.robot.subsystems.elevator.ElevatorConstants.*;

import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.Module;
import frc.robot.subsystems.drive.ModuleIOReal;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.ElevatorIOReal;
import frc.robot.subsystems.elevator.ElevatorIOSim;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
    private final Drive drive;
    private final Elevator elevator;

    private final CommandXboxController controller = new CommandXboxController(driverControllerPort);

    public RobotContainer() {
        switch (currentMode) {
            case REAL:
                drive = new Drive(
                    new GyroIO() {},
                    new Module[] {
                        new Module(new ModuleIOReal(frontLeftDriveId, frontLeftTurnId, frontLeftEncoderId, frontLeftOffset), 0),
                        new Module(new ModuleIOReal(frontRightDriveId, frontRightTurnId, frontRightEncoderId, frontRightOffset), 1),
                        new Module(new ModuleIOReal(backLeftDriveId, backLeftTurnId, backLeftEncoderId, backLeftOffset), 2),
                        new Module(new ModuleIOReal(backRightDriveId, backRightTurnId, backRightEncoderId, backLeftOffset), 3)
                    }
                );
                elevator = new Elevator(new ElevatorIOReal(leftMotorId, rightMotorId));
                break;
            default:
            case SIM:
            case REPLAY:
                drive = new Drive(
                    new GyroIO() {},
                    new Module[] {
                        new Module(new ModuleIOSim(), 0),
                        new Module(new ModuleIOSim(), 1),
                        new Module(new ModuleIOSim(), 2),
                        new Module(new ModuleIOSim(), 3)
                    }
                );
                elevator = new Elevator(new ElevatorIOSim());
                break;
        }

        configureBindings();
    }

    private void configureBindings() {
        drive.setDefaultCommand(
            new RunCommand(
                () -> drive.setSpeedsFieldOriented(
                    new ChassisSpeeds(
                        MathUtil.applyDeadband(-controller.getLeftY(), 0.1) * driveSpeed, 
                        MathUtil.applyDeadband(-controller.getLeftX(), 0.1) * driveSpeed, 
                        MathUtil.applyDeadband(-controller.getRightX(), 0.1) * turnSpeed
                    )
                ),
                drive
            )
        );

        elevator.setDefaultCommand(
            new RunCommand(
                () -> elevator.setPosition(controller.povDown().getAsBoolean() ? 1.3 : (controller.povLeft().getAsBoolean() ? 1.0 : (controller.povUp().getAsBoolean() ? 0.6 : (controller.povRight().getAsBoolean() ? 0.3 : 0.0)))),
                elevator
            )
        );

        // controller.a().whileTrue(elevator.sysIdRoutine());
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
