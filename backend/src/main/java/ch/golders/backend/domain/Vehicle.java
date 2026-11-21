package ch.golders.backend.domain;

public class Vehicle {

    private final String id;
    private final String make;
    private final String model;
    private final int seats;
    private final int doors;
    private final String transmission;
    private final String licensePlate;
    private final double payloadCapacity;
    private final double cargoVolume;
    private final FuelType fuelType;
    private VehicleStatus status;

    public Vehicle(String id, String make, String model, int seats, int doors, String transmission, String licensePlate, double payloadCapacity, double cargoVolume, FuelType fuelType) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.seats = seats;
        this.doors = doors;
        this.transmission = transmission;
        this.licensePlate = licensePlate;
        this.payloadCapacity = payloadCapacity;
        this.cargoVolume = cargoVolume;
        this.fuelType = fuelType;
        this.status = VehicleStatus.AVAILABLE;
    }

    public String getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getSeats() {
        return seats;
    }

    public int getDoors() {
        return doors;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public double getPayloadCapacity() {
        return payloadCapacity;
    }

    public double getCargoVolume() {
        return cargoVolume;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public VehicleStatus getStatus() {
        return status;
    }

}
