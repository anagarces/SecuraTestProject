import { TestBed } from '@angular/core/testing';

import { AdminSubmissionServiceService } from './admin-submission-service.service';

describe('AdminSubmissionServiceService', () => {
  let service: AdminSubmissionServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminSubmissionServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
