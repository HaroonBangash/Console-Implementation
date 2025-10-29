"use client";

import * as React from "react";
import { Building2, LineChart, Users } from "lucide-react";

import { PageHeader } from "@/components/page-header";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { useToast } from "@/components/ui/use-toast";

const users = [
  { id: "admin-user-1", name: "Lina Reyes", role: "Trainer", status: "Active" },
  { id: "admin-user-2", name: "Marcus Quinn", role: "Student", status: "Suspended" },
  { id: "admin-user-3", name: "Amina Hassan", role: "Student", status: "Active" }
];

const partners = [
  { id: "partner-1", name: "Nimbus Labs", contact: "ops@nimbus.example", status: "Active" },
  { id: "partner-2", name: "PulseHQ", contact: "talent@pulse.example", status: "Pending" }
];

export default function AdminDashboardPage() {
  const { addToast } = useToast();

  return (
    <div className="space-y-6">
      <PageHeader
        title="Admin dashboard"
        description="Platform health, partner relationships, and user management."
      />
      <div className="grid gap-6 md:grid-cols-3">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-xl">
              <Users className="h-5 w-5" aria-hidden="true" /> Platform usage
            </CardTitle>
            <CardDescription>Active learners this week</CardDescription>
          </CardHeader>
          <CardContent className="text-3xl font-semibold">1,860</CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-xl">
              <LineChart className="h-5 w-5" aria-hidden="true" /> Placement success rate
            </CardTitle>
            <CardDescription>Quarter-to-date hires</CardDescription>
          </CardHeader>
          <CardContent className="text-3xl font-semibold">68%</CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-xl">
              <Building2 className="h-5 w-5" aria-hidden="true" /> Employer feedback
            </CardTitle>
            <CardDescription>Net satisfaction score</CardDescription>
          </CardHeader>
          <CardContent className="text-3xl font-semibold">4.6 / 5</CardContent>
        </Card>
      </div>
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Users</CardTitle>
            <CardDescription>Activate or suspend learner and trainer accounts.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Name</TableHead>
                  <TableHead>Role</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead className="text-right">Action</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {users.map((user) => (
                  <TableRow key={user.id}>
                    <TableCell>{user.name}</TableCell>
                    <TableCell>{user.role}</TableCell>
                    <TableCell>
                      <Badge variant={user.status === "Active" ? "success" : "warning"}>{user.status}</Badge>
                    </TableCell>
                    <TableCell className="text-right">
                      <Button
                        variant="ghost"
                        onClick={() => addToast({ title: "Status updated", description: `${user.name} toggled.` })}
                      >
                        {user.status === "Active" ? "Suspend" : "Activate"}
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Partnerships</CardTitle>
            <CardDescription>Manage employer onboarding and visibility.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Partner</TableHead>
                  <TableHead>Contact</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead className="text-right">Action</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {partners.map((partner) => (
                  <TableRow key={partner.id}>
                    <TableCell>{partner.name}</TableCell>
                    <TableCell>{partner.contact}</TableCell>
                    <TableCell>
                      <Badge variant={partner.status === "Active" ? "success" : "warning"}>{partner.status}</Badge>
                    </TableCell>
                    <TableCell className="text-right">
                      <Button
                        variant="ghost"
                        onClick={() => addToast({ title: "Partner updated", description: `${partner.name} flagged for follow-up.` })}
                      >
                        Edit
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
            <div className="flex items-center gap-3">
              <Input placeholder="New partner name" aria-label="New partner name" />
              <Button onClick={() => addToast({ title: "Partner added", description: "Draft partnership created." })}>
                Add partner
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
