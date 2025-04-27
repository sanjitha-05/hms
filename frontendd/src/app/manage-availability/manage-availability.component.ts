import { Component, OnInit } from '@angular/core';
 import { DoctorScheduleService } from '../services/doctor-schedule.service';

 interface LocalDate {
   year: number;
   monthValue: number;
   dayOfMonth: number;
 }

 @Component({
   selector: 'app-manage-availability',
   standalone: false,
   templateUrl: './manage-availability.component.html',
   styleUrls: ['./manage-availability.component.css']
 })
 export class ManageAvailabilityComponent implements OnInit {
   doctorId: number | null = null;
   availableDates: LocalDate[] = [];
   showBlockDayPopup: boolean = false;
   selectedDate: LocalDate | null = null;
   //selectedDate: string | null = null;
   showConfirmationPopup: boolean = false;
   showConfirmationnPopup: boolean = false;
   //availableDates: LocalDate[] = []; // For block day
   blockedDates: LocalDate[] = [];   // For unblock day
   //showBlockDayPopup: boolean = false;
   showUnblockDayPopup: boolean = false;
  
   isBlocking: boolean = true;

   constructor(private doctorScheduleService: DoctorScheduleService) {}

   ngOnInit(): void {
     const storedDoctorId = localStorage.getItem('doctorId');
     if (storedDoctorId) {
       this.doctorId = parseInt(storedDoctorId, 10);
     } else {
       console.error('Doctor ID not found in local storage.');
       // Handle this case appropriately (e.g., redirect to login)
     }
   }

   async blockAvailabilityForDay() {
    if (this.doctorId) {
      try {
        const dates = await this.doctorScheduleService.getFreeDates(this.doctorId);
        //console.log('Fetched Available Dates:', dates);
        
        const today = new Date();
        
        this.availableDates = dates.filter((dateString: string) => { 
          const parts = dateString.split('-'); 
          if (parts.length === 3) {
            const year = parseInt(parts[0], 10);
            const month = parseInt(parts[1], 10); // Month from string is 1-based
            const day = parseInt(parts[2], 10);
       
            // Month in JavaScript Date is 0-based, so subtract 1
            const dateObj = new Date(year, month - 1, day);
            return dateObj > today;
          } else {
            console.error('Invalid date string format:', dateString);
            return false; // Skip invalid date strings
          }
        });
        this.showBlockDayPopup = true;
  
      } catch (error) {
        console.error('Error fetching available dates:', error);
      }
    } else {
      console.error('Doctor ID is null, cannot fetch available dates.');
    }
  }


  selectDateToBlock(date: LocalDate) {
    this.selectedDate = date; // Directly assign without conversion
    console.log('Selected Date:', this.selectedDate);
    this.showConfirmationPopup = true; // Show confirmation box instead of directly blocking
  }

confirmBlockDate() {
  if (this.selectedDate && this.doctorId) {
    this.doctorScheduleService.blockDate(this.doctorId, this.selectedDate)
      .then(response => {
        console.log('Date blocked successfully:', response);
        this.showConfirmationPopup = false;
        this.showBlockDayPopup = false; // Close popup after successful action
        this.selectedDate = null;
      })
      .catch(error => console.error('Error blocking date:', error));
  }
}
closeBlockDayPopup() {
  this.showBlockDayPopup = false;
  this.showConfirmationPopup = false;
  this.selectedDate = null;
}


cancelBlockDate() {
  this.showConfirmationPopup = false;
  this.selectedDate = null;
}

async unblockAvailabilityForDay() {
  this.isBlocking = false;
  if (this.doctorId) {
    try {
      const dates = await this.doctorScheduleService.getBlockedDates(this.doctorId);

      const today=new Date();

      // this.blockedDates = dates;
      // this.showUnblockDayPopup = true;
      
this.blockedDates = dates.filter((dateString: string) => {
            const parts = dateString.split('-');
            if (parts.length === 3) {
              const year = parseInt(parts[0], 10);
              const month = parseInt(parts[1], 10); // Month from string is 1-based
              const day = parseInt(parts[2], 10);
  
              // Month in JavaScript Date is 0-based, so subtract 1
              const dateObj = new Date(year, month - 1, day);
              return dateObj >= today;
            } else {
              console.error('Invalid date string format:', dateString);
              return false; // Skip invalid date strings
            }
        });
        this.showUnblockDayPopup = true;
  
    } catch (error) {
      console.error('Error fetching blocked dates:', error);
    }
  } else {
    console.error('Doctor ID is null, cannot fetch blocked dates.');
  }
}

selectDateToUnblock(date: LocalDate) {
  this.selectedDate = date;
  this.isBlocking = false;
  this.showConfirmationnPopup = true;
}

confirmUnblockDate() {
  if (this.selectedDate && this.doctorId) {
    this.doctorScheduleService.unblockDate(this.doctorId, this.selectedDate)
      .then(response => {
        this.closeUnblockDayPopup(); 
      })
      .catch(error => console.error('Error unblocking date:', error));
  }
}
  closeUnblockDayPopup() {
    
    this.showUnblockDayPopup = false;
    this.showConfirmationnPopup = false;
    this.selectedDate = null;
  }
  cancelConfirmation() {
    this.showConfirmationnPopup = false;
    this.selectedDate = null;
  }

   blockAvailabilityForDateTime() {
     console.log('Block Availability for Particular Date and Time clicked in ManageAvailabilityComponent', this.doctorId);
     // Implement logic here to show a date and time selection interface
     // and call a service to block the specific date and time
   }

   unblockAvailabilityForDateTime() {
     console.log('Unblock Availability for Particular Date and Time clicked in ManageAvailabilityComponent', this.doctorId);
     // Implement logic here to show a date and time selection interface
     // and call a service to unblock the specific date and time
   }
 }