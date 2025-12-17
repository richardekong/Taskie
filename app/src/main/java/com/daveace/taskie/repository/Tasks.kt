import android.os.Build
import androidx.annotation.RequiresApi
import com.daveace.taskie.model.Task
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
val tasks = listOf(
    Task(
        1, "Write CV",
        "Update CV with latest projects",
        "Pending",
        LocalDateTime.now().plusDays(2)
    ), Task(
        2,
        "NHS Application",
        "Submit application for NHS backend role",
        "In Progress",
        LocalDateTime.now().plusDays(5)
    ), Task(
        3,
        "Spring Boot Lab",
        "Complete interactive lab on REST APIs",
        "Pending",
        LocalDateTime.now().plusDays(3)
    ), Task(
        4,
        "Compose Theming",
        "Implement dynamic color fallback logic",
        "In Progress",
        LocalDateTime.now().plusDays(1)
    ), Task(
        5,
        "Accessibility Review",
        "Check UI against WCAG guidelines",
        "Pending",
        LocalDateTime.now().plusDays(7)
    ), Task(
        6,
        "Unit Tests",
        "Write tests for recruitment web app",
        "Pending",
        LocalDateTime.now().plusDays(4)
    ), Task(
        7,
        "Sales Diary Fix",
        "Debug edge case in diary platform",
        "Completed",
        LocalDateTime.now().minusDays(1)
    ), Task(
        8,
        "Cloud API Integration",
        "Connect app to external weather API",
        "Pending",
        LocalDateTime.now().plusDays(6)
    ), Task(
        9,
        "Compose IconButton",
        "Add borders and accessibility labels",
        "In Progress",
        LocalDateTime.now().plusDays(2)
    ), Task(
        10,
        "Wrap Content Check",
        "Test layout edge cases in Compose",
        "Pending",
        LocalDateTime.now().plusDays(3)
    ), Task(
        11,
        "Backend Refactor",
        "Refactor service layer for clarity",
        "Pending",
        LocalDateTime.now().plusDays(8)
    ), Task(
        12,
        "NHS Cover Letter",
        "Draft tailored cover letter for NHS Jobs",
        "Pending",
        LocalDateTime.now().plusDays(5)
    ), Task(
        13,
        "Trac Profile",
        "Update Trac personal statement",
        "Pending",
        LocalDateTime.now().plusDays(4)
    ), Task(
        14,
        "Compose Preview",
        "Fix caching issue in Android Studio preview",
        "Completed",
        LocalDateTime.now().minusDays(2)
    ), Task(
        15,
        "Recruitment Portal",
        "Add candidate filtering logic",
        "In Progress",
        LocalDateTime.now().plusDays(6)
    ), Task(
        16,
        "Navigation System",
        "Test fallback routes in navigation app",
        "Pending",
        LocalDateTime.now().plusDays(9)
    ), Task(
        17,
        "Workflow Script",
        "Automate repetitive backend tasks",
        "Pending",
        LocalDateTime.now().plusDays(10)
    ), Task(
        18,
        "Compose Geometry",
        "Validate UI geometry with custom overrides",
        "In Progress",
        LocalDateTime.now().plusDays(2)
    ), Task(
        19,
        "Support Reflection",
        "Write reflective practice notes for care role",
        "Pending",
        LocalDateTime.now().plusDays(7)
    ), Task(
        20,
        "Portfolio Update",
        "Add MSc project highlights to portfolio",
        "Pending",
        LocalDateTime.now().plusDays(12)
    )
)

