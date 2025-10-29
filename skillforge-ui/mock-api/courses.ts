export type Course = {
  id: string;
  programId: string;
  title: string;
  progress: number;
  status: "in-progress" | "completed" | "not-started";
  nextStep?: string;
};

export const courses: Course[] = [
  {
    id: "course-cloud-1",
    programId: "cloud-fundamentals",
    title: "Module 3: Scaling Observability",
    progress: 65,
    status: "in-progress",
    nextStep: "Virtual Lab"
  },
  {
    id: "course-cyber-1",
    programId: "cyber-blue-team",
    title: "Module 2: Threat Detection Playbooks",
    progress: 40,
    status: "in-progress",
    nextStep: "Virtual Lab"
  },
  {
    id: "course-ai-1",
    programId: "ai-customer-success",
    title: "Module 1: Intelligent Health Checks",
    progress: 100,
    status: "completed"
  }
];
