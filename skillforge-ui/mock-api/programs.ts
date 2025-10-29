export type Program = {
  id: string;
  title: string;
  industry: "IT" | "Business" | "Hospitality" | "Healthcare";
  durationWeeks: number;
  accreditation: boolean;
  outcome: "Accreditation" | "Certification";
  startDate: string;
  summary: string;
  competencies: string[];
  hasVirtualLab: boolean;
};

export const programs: Program[] = [
  {
    id: "cloud-fundamentals",
    title: "Cloud Operations Fundamentals",
    industry: "IT",
    durationWeeks: 6,
    accreditation: true,
    outcome: "Certification",
    startDate: "2024-06-03",
    summary: "Deploy resilient cloud workloads with hands-on labs and industry mentors.",
    competencies: ["Infrastructure as Code", "Monitoring", "Incident Response"],
    hasVirtualLab: true
  },
  {
    id: "ai-customer-success",
    title: "AI-enabled Customer Success",
    industry: "Business",
    durationWeeks: 5,
    accreditation: false,
    outcome: "Certification",
    startDate: "2024-06-10",
    summary: "Blend data storytelling with automation to scale customer loyalty.",
    competencies: ["Journey Mapping", "Automation", "Customer Insights"],
    hasVirtualLab: false
  },
  {
    id: "cyber-blue-team",
    title: "Cybersecurity Blue Team Analyst",
    industry: "IT",
    durationWeeks: 8,
    accreditation: true,
    outcome: "Accreditation",
    startDate: "2024-07-01",
    summary: "Monitor threats, execute incident playbooks, and master digital forensics.",
    competencies: ["Threat Hunting", "SIEM Analysis", "Forensic Readiness"],
    hasVirtualLab: true
  },
  {
    id: "sustainable-operations",
    title: "Sustainable Operations Leadership",
    industry: "Business",
    durationWeeks: 6,
    accreditation: false,
    outcome: "Certification",
    startDate: "2024-06-24",
    summary: "Design low-carbon processes with real-time analytics and stakeholder labs.",
    competencies: ["ESG Reporting", "Circular Design", "Change Management"],
    hasVirtualLab: false
  },
  {
    id: "smart-hospitality",
    title: "Smart Hospitality Management",
    industry: "Hospitality",
    durationWeeks: 6,
    accreditation: true,
    outcome: "Accreditation",
    startDate: "2024-06-17",
    summary: "Deliver personalised guest experiences with data-led service design.",
    competencies: ["Service Automation", "Revenue Analytics", "Experience Design"],
    hasVirtualLab: true
  },
  {
    id: "clinical-data-analytics",
    title: "Clinical Data Analytics",
    industry: "Healthcare",
    durationWeeks: 7,
    accreditation: true,
    outcome: "Certification",
    startDate: "2024-06-17",
    summary: "Unlock better health outcomes through interoperable data pipelines.",
    competencies: ["HL7 Standards", "Data Quality", "Predictive Models"],
    hasVirtualLab: false
  },
  {
    id: "hospitality-leadership",
    title: "Hospitality Leadership Accelerator",
    industry: "Hospitality",
    durationWeeks: 5,
    accreditation: false,
    outcome: "Certification",
    startDate: "2024-07-08",
    summary: "Build resilient teams, operational agility, and brand advocacy.",
    competencies: ["Team Coaching", "Financial Acumen", "Brand Experience"],
    hasVirtualLab: false
  },
  {
    id: "digital-health-innovation",
    title: "Digital Health Innovation Studio",
    industry: "Healthcare",
    durationWeeks: 8,
    accreditation: true,
    outcome: "Accreditation",
    startDate: "2024-07-15",
    summary: "Prototype digital health solutions with clinical mentors and regulatory labs.",
    competencies: ["Service Design", "Compliance", "Product Strategy"],
    hasVirtualLab: true
  }
];
