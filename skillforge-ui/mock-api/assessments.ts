export type Assessment = {
  id: string;
  title: string;
  type: "quiz" | "project";
  dueDate: string;
  status: "upcoming" | "submitted" | "released" | "awaiting";
  score?: number;
  maxScore?: number;
  relatedProgram: string;
};

export const assessments: Assessment[] = [
  {
    id: "quiz-observability",
    title: "Observability Foundations Quiz",
    type: "quiz",
    dueDate: "2024-05-24",
    status: "upcoming",
    relatedProgram: "Cloud Operations Fundamentals"
  },
  {
    id: "project-siem",
    title: "Blue Team Incident Simulation",
    type: "project",
    dueDate: "2024-05-27",
    status: "upcoming",
    relatedProgram: "Cybersecurity Blue Team Analyst"
  },
  {
    id: "quiz-ai",
    title: "Customer Health Metrics",
    type: "quiz",
    dueDate: "2024-05-10",
    status: "released",
    score: 18,
    maxScore: 20,
    relatedProgram: "AI-enabled Customer Success"
  }
];
