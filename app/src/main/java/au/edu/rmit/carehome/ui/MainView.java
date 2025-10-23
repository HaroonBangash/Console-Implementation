package au.edu.rmit.carehome.ui;

import au.edu.rmit.carehome.app.CareHomeContainer;
import au.edu.rmit.carehome.common.AuthorizationException;
import au.edu.rmit.carehome.domain.facility.Bed;
import au.edu.rmit.carehome.domain.facility.Room;
import au.edu.rmit.carehome.domain.facility.Ward;
import au.edu.rmit.carehome.domain.medication.Prescription;
import au.edu.rmit.carehome.domain.medication.PrescriptionItem;
import au.edu.rmit.carehome.domain.resident.Gender;
import au.edu.rmit.carehome.domain.resident.Resident;
import au.edu.rmit.carehome.domain.staff.Role;
import au.edu.rmit.carehome.domain.staff.Staff;
import au.edu.rmit.carehome.services.MedicationService;
import au.edu.rmit.carehome.services.MedicationService.DueDose;
import au.edu.rmit.carehome.services.MedicationService.PrescriptionItemRequest;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Design decisions: Programmatic JavaFX view combining ward/bed map with contextual actions, keeping
 * controllers minimal by embedding logic in this cohesive presenter.
 */
public class MainView {
    private final CareHomeContainer container;
    private final BorderPane root;
    private final GridPane bedGrid;
    private final TextArea detailArea;
    private final Label statusLabel;
    private final Button loginButton;
    private final Button logoutButton;
    private final Button addResidentButton;
    private final Button moveResidentButton;
    private final Button dischargeResidentButton;
    private final Button addStaffButton;
    private final Button resetPasswordButton;
    private final Button prescribeButton;
    private final Button administerButton;
    private final Button dueDosesButton;
    private UUID selectedBedId;
    private final Map<UUID, Button> bedButtons = new HashMap<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public MainView(CareHomeContainer container) {
        this.container = container;
        this.root = new BorderPane();
        this.bedGrid = new GridPane();
        this.bedGrid.setHgap(10);
        this.bedGrid.setVgap(6);
        this.bedGrid.setPadding(new Insets(10));
        this.detailArea = new TextArea();
        this.detailArea.setEditable(false);
        this.detailArea.setPrefWidth(350);
        this.statusLabel = new Label("Not signed in");
        this.loginButton = new Button("Login");
        this.logoutButton = new Button("Logout");
        this.addResidentButton = new Button("Admit Resident");
        this.moveResidentButton = new Button("Move Resident");
        this.dischargeResidentButton = new Button("Discharge Resident");
        this.addStaffButton = new Button("Add Staff");
        this.resetPasswordButton = new Button("Reset Password");
        this.prescribeButton = new Button("New Prescription");
        this.administerButton = new Button("Record Dose");
        this.dueDosesButton = new Button("Due Today");
        buildLayout();
        registerActions();
        refreshBeds();
        updateActionVisibility();
    }

    public BorderPane getRoot() {
        return root;
    }

    public void promptLogin(Stage owner) {
        login(owner);
    }

    private void buildLayout() {
        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(statusLabel, new Separator(), loginButton, logoutButton,
                new Separator(), addResidentButton, moveResidentButton, dischargeResidentButton,
                new Separator(), addStaffButton, resetPasswordButton,
                new Separator(), prescribeButton, administerButton, dueDosesButton);
        root.setTop(toolBar);

        ScrollPane bedScroll = new ScrollPane(bedGrid);
        bedScroll.setFitToWidth(true);
        root.setCenter(bedScroll);

        VBox sidePanel = new VBox(10, new Label("Resident Details"), detailArea);
        sidePanel.setPadding(new Insets(10));
        root.setRight(sidePanel);
    }

    private void registerActions() {
        loginButton.setOnAction(event -> login(loginButton.getScene().getWindow() instanceof Stage stage ? stage : null));
        logoutButton.setOnAction(event -> {
            container.getAuthContext().logout();
            statusLabel.setText("Not signed in");
            updateActionVisibility();
        });
        addResidentButton.setOnAction(event -> onAdmitResident());
        moveResidentButton.setOnAction(event -> onMoveResident());
        dischargeResidentButton.setOnAction(event -> onDischargeResident());
        addStaffButton.setOnAction(event -> onAddStaff());
        resetPasswordButton.setOnAction(event -> onResetPassword());
        prescribeButton.setOnAction(event -> onNewPrescription());
        administerButton.setOnAction(event -> onRecordDose());
        dueDosesButton.setOnAction(event -> onShowDueDoses());
    }

    private void refreshBeds() {
        bedGrid.getChildren().clear();
        bedButtons.clear();
        int row = 0;
        for (Ward ward : container.getCareHome().getWards()) {
            Label wardLabel = new Label(ward.getName());
            wardLabel.getStyleClass().add("heading");
            bedGrid.add(wardLabel, 0, row++, 5, 1);
            for (Room room : ward.getRooms()) {
                bedGrid.add(new Label("Room " + room.getId().toString().substring(0, 8)), 0, row);
                int col = 1;
                for (Bed bed : room.getBeds()) {
                    Button button = createBedButton(bed);
                    bedGrid.add(button, col++, row);
                }
                row++;
            }
        }
    }

    private Button createBedButton(Bed bed) {
        Button button = new Button();
        button.setPrefWidth(160);
        button.setWrapText(true);
        updateBedButtonText(button, bed);
        button.setOnAction(event -> selectBed(bed));
        bedButtons.put(bed.getId(), button);
        return button;
    }

    private void updateBedButtonText(Button button, Bed bed) {
        Optional<UUID> occupantId = bed.getOccupantResidentId();
        String occupantText = occupantId.flatMap(id -> Optional.ofNullable(container.getCareHome().getResidents().get(id)))
                .map(Resident::getName)
                .orElse("Vacant");
        button.setText("Bed " + bed.getId().toString().substring(0, 6) + "\n" + occupantText);
        String colour = bed.getOccupantResidentId().isPresent()
                ? (bed.getGenderTag() == Gender.MALE ? "#1E88E5" : "#E53935")
                : "#9E9E9E";
        button.setStyle("-fx-background-color:" + colour + "; -fx-text-fill: white;");
        if (bed.getId().equals(selectedBedId)) {
            button.setStyle(button.getStyle() + "-fx-border-color: gold; -fx-border-width: 3;");
        }
    }

    private void selectBed(Bed bed) {
        selectedBedId = bed.getId();
        bedButtons.forEach((id, button) -> container.getCareHome().findBed(id)
                .ifPresent(found -> updateBedButtonText(button, found)));
        showDetails(bed);
        updateActionVisibility();
    }

    private void showDetails(Bed bed) {
        StringBuilder builder = new StringBuilder();
        builder.append("Bed ID: ").append(bed.getId()).append('\n');
        builder.append("Gender: ").append(bed.getGenderTag()).append('\n');
        Optional<Resident> resident = bed.getOccupantResidentId()
                .map(id -> container.getCareHome().getResidents().get(id));
        if (resident.isPresent()) {
            Resident r = resident.get();
            builder.append("Resident: ").append(r.getName()).append('\n');
            builder.append("DOB: ").append(r.getDateOfBirth().format(dateFormatter)).append('\n');
            builder.append("Conditions: ").append(String.join(", ", r.getConditionFlags())).append('\n');
            builder.append("History:\n");
            for (String entry : r.getHistoryEntries()) {
                builder.append(" - ").append(entry).append('\n');
            }
            builder.append("Prescriptions:\n");
            List<Prescription> prescriptions = container.getCareHome().getPrescriptions().stream()
                    .filter(p -> p.getResidentId().equals(r.getId()))
                    .collect(Collectors.toList());
            for (Prescription prescription : prescriptions) {
                builder.append(" * ").append(prescription.getId()).append(" at ")
                        .append(prescription.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append('\n');
                for (PrescriptionItem item : prescription.getItems()) {
                    builder.append("    - ").append(item.getDrugName()).append(" ")
                            .append(item.getDose()).append(item.getUnit()).append(" times ")
                            .append(item.getScheduleTimes().stream().map(timeFormatter::format).collect(Collectors.joining(", ")))
                            .append('\n');
                }
            }
        } else {
            builder.append("Resident: Vacant\n");
        }
        detailArea.setText(builder.toString());
    }

    private void updateActionVisibility() {
        Staff current = container.getAuthContext().currentUser().orElse(null);
        boolean loggedIn = current != null;
        loginButton.setDisable(loggedIn);
        logoutButton.setDisable(!loggedIn);
        addResidentButton.setDisable(!loggedIn || current.getRole() != Role.MANAGER || !isSelectedBedVacant());
        moveResidentButton.setDisable(!loggedIn || current.getRole() != Role.MANAGER || getSelectedResident().isEmpty());
        dischargeResidentButton.setDisable(!loggedIn || current.getRole() != Role.MANAGER || getSelectedResident().isEmpty());
        addStaffButton.setDisable(!loggedIn || current.getRole() != Role.MANAGER);
        resetPasswordButton.setDisable(!loggedIn || current.getRole() != Role.MANAGER);
        prescribeButton.setDisable(!loggedIn || current.getRole() != Role.DOCTOR || getSelectedResident().isEmpty());
        administerButton.setDisable(!loggedIn || current.getRole() != Role.NURSE || getSelectedResident().isEmpty());
        dueDosesButton.setDisable(!loggedIn || current.getRole() != Role.NURSE);
    }

    private Optional<Resident> getSelectedResident() {
        if (selectedBedId == null) {
            return Optional.empty();
        }
        return container.getCareHome().findBed(selectedBedId)
                .flatMap(bed -> bed.getOccupantResidentId())
                .map(id -> container.getCareHome().getResidents().get(id));
    }

    private boolean isSelectedBedVacant() {
        return selectedBedId != null && container.getCareHome().findBed(selectedBedId)
                .map(bed -> bed.getOccupantResidentId().isEmpty())
                .orElse(false);
    }

    private void login(Stage stage) {
        Dialog<Credentials> dialog = new Dialog<>();
        dialog.setTitle("Staff Login");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        grid.addRow(0, new Label("Username"), usernameField);
        grid.addRow(1, new Label("Password"), passwordField);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(button -> button == ButtonType.OK
                ? new Credentials(usernameField.getText(), passwordField.getText()) : null);
        dialog.initOwner(stage);
        Optional<Credentials> result = dialog.showAndWait();
        result.ifPresent(credentials -> {
            try {
                Staff staff = container.getAuthenticationService().login(credentials.username(), credentials.password());
                container.getAuthContext().login(staff);
                statusLabel.setText("Logged in as " + staff.getName() + " (" + staff.getRole() + ")");
            } catch (AuthorizationException ex) {
                showAlert(Alert.AlertType.ERROR, "Login failed", ex.getMessage());
            }
            updateActionVisibility();
        });
    }

    private void onAdmitResident() {
        container.getAuthContext().currentUser().ifPresent(manager -> {
            Bed bed = container.getCareHome().findBed(selectedBedId).orElse(null);
            if (bed == null) {
                return;
            }
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Admit Resident");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(10));
            TextField nameField = new TextField();
            TextField dobField = new TextField();
            dobField.setPromptText("yyyy-MM-dd");
            TextField conditionsField = new TextField();
            grid.addRow(0, new Label("Name"), nameField);
            grid.addRow(1, new Label("DOB"), dobField);
            grid.addRow(2, new Label("Conditions"), conditionsField);
            grid.addRow(3, new Label("Gender"), new Label(bed.getGenderTag().name()));
            dialog.getDialogPane().setContent(grid);
            Optional<ButtonType> response = dialog.showAndWait();
            if (response.isPresent() && response.get() == ButtonType.OK) {
                try {
                    Resident resident = new Resident(UUID.randomUUID(), nameField.getText(), bed.getGenderTag(),
                            LocalDate.parse(dobField.getText(), dateFormatter),
                            Arrays.stream(conditionsField.getText().split(","))
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .collect(Collectors.toSet()));
                    container.getAdmissionService().admitResident(manager, resident, bed.getId());
                    refreshBeds();
                    selectBed(bed);
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Admission failed", ex.getMessage());
                }
            }
        });
    }

    private void onMoveResident() {
        Optional<Resident> resident = getSelectedResident();
        if (resident.isEmpty()) {
            return;
        }
        List<Bed> availableBeds = container.getCareHome().getAllBeds().stream()
                .filter(b -> b.getOccupantResidentId().isEmpty() && b.getGenderTag() == resident.get().getGender())
                .collect(Collectors.toList());
        if (availableBeds.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Move Resident", "No compatible beds available");
            return;
        }
        ChoiceDialog<Bed> dialog = new ChoiceDialog<>(availableBeds.get(0), availableBeds);
        dialog.setTitle("Move Resident");
        dialog.setHeaderText("Select a new bed");
        dialog.setContentText("Bed:");
        dialog.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Bed bed) {
                return describeBed(bed);
            }

            @Override
            public Bed fromString(String string) {
                return availableBeds.stream().filter(b -> describeBed(b).equals(string)).findFirst().orElse(null);
            }
        });
        dialog.showAndWait().ifPresent(target -> {
            try {
                Staff manager = container.getAuthContext().currentUser().orElseThrow();
                container.getAdmissionService().moveResident(manager, resident.get().getId(), target.getId());
                refreshBeds();
                selectBed(target);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Move failed", ex.getMessage());
            }
        });
    }

    private void onDischargeResident() {
        getSelectedResident().ifPresent(resident -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Discharge " + resident.getName() + "?",
                    ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirm Discharge");
            alert.showAndWait().ifPresent(button -> {
                if (button == ButtonType.YES) {
                    try {
                        Staff manager = container.getAuthContext().currentUser().orElseThrow();
                        container.getAdmissionService().dischargeResident(manager, resident.getId());
                        refreshBeds();
                        detailArea.clear();
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Discharge failed", ex.getMessage());
                    }
                }
            });
        });
    }

    private void onAddStaff() {
        Staff manager = container.getAuthContext().currentUser().orElse(null);
        if (manager == null) {
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Staff Member");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        TextField nameField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        ComboBox<Role> roleCombo = new ComboBox<>(FXCollections.observableArrayList(Role.values()));
        roleCombo.getSelectionModel().select(Role.NURSE);
        grid.addRow(0, new Label("Name"), nameField);
        grid.addRow(1, new Label("Username"), usernameField);
        grid.addRow(2, new Label("Password"), passwordField);
        grid.addRow(3, new Label("Role"), roleCombo);
        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    container.getStaffService().createStaff(manager, roleCombo.getValue(), nameField.getText(),
                            usernameField.getText(), passwordField.getText());
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Add staff failed", ex.getMessage());
                }
            }
        });
    }

    private void onResetPassword() {
        Staff manager = container.getAuthContext().currentUser().orElse(null);
        if (manager == null) {
            return;
        }
        List<Staff> staffList = new ArrayList<>(container.getCareHome().getStaff().values());
        if (staffList.isEmpty()) {
            return;
        }
        ChoiceDialog<Staff> dialog = new ChoiceDialog<>(staffList.get(0), staffList);
        dialog.setTitle("Reset Credentials");
        dialog.setHeaderText("Select staff to update");
        dialog.setContentText("Staff:");
        dialog.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Staff staff) {
                return staff.getName() + " (" + staff.getRole() + ")";
            }

            @Override
            public Staff fromString(String string) {
                return staffList.stream().filter(staff -> toString(staff).equals(string)).findFirst().orElse(null);
            }
        });
        dialog.showAndWait().ifPresent(staff -> {
            TextInputDialog usernameDialog = new TextInputDialog(staff.getUsername());
            usernameDialog.setTitle("Update Username");
            usernameDialog.setHeaderText("Update username for " + staff.getName());
            Optional<String> usernameResult = usernameDialog.showAndWait();
            if (usernameResult.isEmpty()) {
                return;
            }
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Reset Password");
            passwordDialog.setHeaderText("New password for " + staff.getName());
            passwordDialog.getEditor().setPromptText("Leave blank to keep current password");
            Optional<String> passwordResult = passwordDialog.showAndWait();
            try {
                container.getStaffService().updateCredentials(manager, staff.getId(), usernameResult.get(),
                        passwordResult.orElse(""));
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Update credentials failed", ex.getMessage());
            }
        });
    }

    private void onNewPrescription() {
        Optional<Resident> resident = getSelectedResident();
        if (resident.isEmpty()) {
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("New Prescription");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        TextField drugField = new TextField();
        TextField doseField = new TextField();
        TextField unitField = new TextField();
        TextField timesField = new TextField();
        timesField.setPromptText("HH:mm,HH:mm");
        grid.addRow(0, new Label("Drug"), drugField);
        grid.addRow(1, new Label("Dose"), doseField);
        grid.addRow(2, new Label("Unit"), unitField);
        grid.addRow(3, new Label("Times"), timesField);
        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    Staff doctor = container.getAuthContext().currentUser().orElseThrow();
                    List<LocalTime> times = Arrays.stream(timesField.getText().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(time -> LocalTime.parse(time, timeFormatter))
                            .collect(Collectors.toList());
                    List<PrescriptionItemRequest> requests = List.of(
                            new PrescriptionItemRequest(drugField.getText(), Double.parseDouble(doseField.getText()),
                                    unitField.getText(), times));
                    container.getMedicationService().createPrescription(doctor, resident.get().getId(), requests);
                    refreshBeds();
                    container.getCareHome().findBed(selectedBedId).ifPresent(this::showDetails);
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Prescription failed", ex.getMessage());
                }
            }
        });
    }

    private void onRecordDose() {
        Optional<Resident> resident = getSelectedResident();
        if (resident.isEmpty()) {
            return;
        }
        List<PrescriptionItem> items = container.getCareHome().getPrescriptions().stream()
                .filter(p -> p.getResidentId().equals(resident.get().getId()))
                .flatMap(p -> p.getItems().stream())
                .collect(Collectors.toList());
        if (items.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Record Dose", "No prescriptions found");
            return;
        }
        ChoiceDialog<PrescriptionItem> itemDialog = new ChoiceDialog<>(items.get(0), items);
        itemDialog.setTitle("Select Medication");
        itemDialog.setHeaderText("Select prescription item");
        itemDialog.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(PrescriptionItem item) {
                return item.getDrugName() + " " + item.getDose() + item.getUnit();
            }

            @Override
            public PrescriptionItem fromString(String string) {
                return items.stream().filter(item -> toString(item).equals(string)).findFirst().orElse(null);
            }
        });
        itemDialog.showAndWait().ifPresent(item -> {
            TextInputDialog timeDialog = new TextInputDialog(LocalTime.now().format(timeFormatter));
            timeDialog.setTitle("Scheduled Time");
            timeDialog.setHeaderText("Scheduled time for dose");
            Optional<String> timeResult = timeDialog.showAndWait();
            if (timeResult.isEmpty()) {
                return;
            }
            TextInputDialog notesDialog = new TextInputDialog();
            notesDialog.setTitle("Notes");
            notesDialog.setHeaderText("Administration notes (optional)");
            Optional<String> notesResult = notesDialog.showAndWait();
            try {
                Staff nurse = container.getAuthContext().currentUser().orElseThrow();
                container.getMedicationService().recordAdministration(nurse, resident.get().getId(), item.getId(),
                        LocalTime.parse(timeResult.get(), timeFormatter), notesResult.orElse(""));
                refreshBeds();
                container.getCareHome().findBed(selectedBedId).ifPresent(this::showDetails);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Administration failed", ex.getMessage());
            }
        });
    }

    private void onShowDueDoses() {
        List<DueDose> due = container.getMedicationService().getDueDoses(LocalDate.now());
        if (due.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Due Doses", "No outstanding doses for today");
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (DueDose dose : due) {
            builder.append(dose.resident().getName()).append(" - ")
                    .append(dose.item().getDrugName()).append(" at ")
                    .append(dose.scheduledTime().format(timeFormatter)).append('\n');
        }
        showAlert(Alert.AlertType.INFORMATION, "Due Doses", builder.toString());
    }

    private String describeBed(Bed bed) {
        String wardName = container.getCareHome().findRoom(bed.getRoomId())
                .flatMap(room -> container.getCareHome().findWard(room.getWardId()))
                .map(Ward::getName)
                .orElse("Ward");
        return wardName + " - " + bed.getRoomId().toString().substring(0, 6) + " - " + bed.getId().toString().substring(0, 6);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private record Credentials(String username, String password) {
    }
}
