import EmployeePairDisplay from "@/components/employee-pair-display";

export default function Home() {
    return (
        <main className="container mx-auto p-8">
            <h1 className="text-3xl mb-8">Employee Collaboration Time</h1>
            <EmployeePairDisplay/>
        </main>
    );
}
