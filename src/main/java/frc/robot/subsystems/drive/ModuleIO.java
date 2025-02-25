package frc.robot.subsystems.drive;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public interface ModuleIO {
    @AutoLog
    public class ModuleIOInputs {
        public double drivePosition = 0.0;
        public double driveVelocity = 0.0;
        public double turnPosition = 0.0;
        public double turnVelocity = 0.0;

        public double driveVoltage = 0.0;
        public double driveCurrent = 0.0;
        public double turnVoltage = 0.0;
        public double turnCurrent = 0.0;
    }

    public default void updateInputs(ModuleIOInputs inputs) {}

    public default void setDriveVelocity(double velocity, double ffVoltage) {}

    public default void setTurnPosition(double position, double ffVoltage) {}
}
