export type Job = {
  id: string;
  title: string;
  company: string;
  location: "Remote" | "On-site";
  role: string;
  requiredCerts: string[];
  description: string;
};

export const jobs: Job[] = [
  {
    id: "job-cloud-analyst",
    title: "Cloud Operations Analyst",
    company: "Nimbus Labs",
    location: "Remote",
    role: "Cloud Engineering",
    requiredCerts: ["Cloud Operations Fundamentals"],
    description: "Operate observability stacks and automate incident response playbooks."
  },
  {
    id: "job-customer-success",
    title: "Customer Success Strategist",
    company: "PulseHQ",
    location: "Remote",
    role: "Customer Success",
    requiredCerts: ["AI-enabled Customer Success"],
    description: "Design AI-driven engagement journeys and health scoring models."
  },
  {
    id: "job-lab-coordinator",
    title: "Virtual Lab Coordinator",
    company: "SecureWorks",
    location: "On-site",
    role: "Cybersecurity",
    requiredCerts: ["Cybersecurity Blue Team Analyst"],
    description: "Facilitate blue team lab cohorts and mentor defensive automation."
  },
  {
    id: "job-clinical-data",
    title: "Clinical Data Specialist",
    company: "WellSpring Health",
    location: "On-site",
    role: "Healthcare Analytics",
    requiredCerts: ["Clinical Data Analytics"],
    description: "Maintain data governance and build predictive pathways with clinicians."
  }
];
