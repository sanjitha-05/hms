// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-features-section',
//   standalone: false,
//   templateUrl: './features-section.component.html',
//   styleUrl: './features-section.component.css'
// })
// export class FeaturesSectionComponent {

// }
import { Component } from '@angular/core';

@Component({
  selector: 'app-features-section',
  standalone: false,
  templateUrl: './features-section.component.html',
  styleUrls: ['./features-section.component.css']
})
export class FeaturesSectionComponent {
  features = [
    {
      icon: '📅',
      title: 'Easy Appointment Scheduling',
      description: 'Book, reschedule, or cancel appointments with your preferred doctors online in seconds.'
    },
    {
      icon: '👨‍⚕️',
      title: 'Doctor Schedule Management',
      description: 'View real-time availability of doctors and choose time slots that work best for you.'
    },
    {
      icon: '📋',
      title: 'Medical History Tracking',
      description: 'Access your complete medical records, treatments, and diagnoses anytime, anywhere.'
    },
    {
      icon: '🔔',
      title: 'Smart Notifications',
      description: 'Receive timely reminders for upcoming appointments and medication schedules.'
    }
  ];
}
